package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.request.TastingNoteWriteRequest;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.factory.FlavorLevelFactory;
import com.juu.juulabel.api.factory.SensoryLevelFactory;
import com.juu.juulabel.api.factory.SliceResponseFactory;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.*;
import com.juu.juulabel.domain.embedded.AlcoholicDrinksSnapshot;
import com.juu.juulabel.domain.embedded.Flavor;
import com.juu.juulabel.domain.embedded.Sensory;
import com.juu.juulabel.domain.entity.alcohol.*;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.enums.Rateable;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorType;
import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import com.juu.juulabel.domain.repository.reader.*;
import com.juu.juulabel.domain.repository.writer.TastingNoteWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TastingNoteService {

    private final TastingNoteReader tastingNoteReader;
    private final AlcoholTypeReader alcoholTypeReader;
    private final AlcoholicDrinksReader alcoholicDrinksReader;
    private final AlcoholTypeColorReader alcoholTypeColorReader;
    private final AlcoholTypeScentReader alcoholTypeScentReader;
    private final AlcoholTypeSensoryReader alcoholTypeSensoryReader;

    private final TastingNoteWriter tastingNoteWriter;

    private final FlavorLevelFactory flavorLevelFactory;
    private final SensoryLevelFactory sensoryLevelFactory;

    @Transactional(readOnly = true)
    public AlcoholDrinksListResponse searchAlcoholDrinksList(final SearchAlcoholDrinksListRequest request) {
        final Slice<AlcoholicDrinksSummary> alcoholicDrinks = tastingNoteReader.getAllAlcoholicDrinks(request.search(), request.lastAlcoholicDrinksName(), request.pageSize());
        return SliceResponseFactory.create(
                AlcoholDrinksListResponse.class,
                alcoholicDrinks.isLast(),
                alcoholicDrinks.getContent()
        );
    }

    @Transactional(readOnly = true)
    public TastingNoteColorListResponse loadTastingNoteColorsList(final Long alcoholTypeId) {
        final List<ColorInfo> colors = alcoholTypeColorReader.getAllColorInfoByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteColorListResponse(colors);
    }

    @Transactional(readOnly = true)
    public TastingNoteSensoryListResponse loadTastingNoteSensoryList(final Long alcoholTypeId) {
        final List<SensoryType> sensoryTypes = alcoholTypeSensoryReader.getAllSensoryTypesByAlcoholTypeId(alcoholTypeId);
        final List<SensoryLevel> sensoryLevels = sensoryLevelFactory.getAllSensoryLevel(sensoryTypes);
        return new TastingNoteSensoryListResponse(sensoryLevels);
    }

    @Transactional(readOnly = true)
    public TastingNoteScentListResponse loadTastingNoteScentList(final Long alcoholTypeId) {
        final List<CategoryWithScentSummary> categories = alcoholTypeScentReader.getAllCategoryWithScentByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteScentListResponse(categories);
    }

    @Transactional(readOnly = true)
    public TastingNoteFlavorListResponse loadTastingNoteFlavorList() {
        final List<FlavorType> flavorTypes = Arrays.asList(FlavorType.values());
        final List<FlavorLevel> flavorLevels = flavorLevelFactory.getAllFlavorLevel(flavorTypes);
        return new TastingNoteFlavorListResponse(flavorLevels);
    }

    @Transactional
    public TastingNoteWriteResponse write(final Member loginMember, final TastingNoteWriteRequest request) {
        // 1. 전통주 정보 입력 (OD, UD)
        final Long alcoholTypeId = request.alcoholTypeId();
        final AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);
        final AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getByIdOrElseNull(request.alcoholicDrinksId());
        final AlcoholicDrinksSnapshot alcoholicDrinksInfo = AlcoholicDrinksSnapshot.fromDto(request.alcoholicDrinksDetails());

        // 2. 시각, 촉각, 후각, 미각 그래프 입력
        // 2-1. 시각 정보 유효성 검사 (주종)
        final Color color = getValidColorOrElseThrow(alcoholTypeId, request.colorId());

        // 2-2. 촉각 및 미각 정보 유효성 검사 (주종)
        final Map<SensoryType, String> sensoryMap = request.sensoryMap();
        final Map<FlavorType, String> flavorMap = request.flavorMap();
        validateSensoryType(alcoholTypeId, sensoryMap);
        validateFlavorType(flavorMap);

        final Sensory sensory = convertMapToSensory(sensoryMap);
        final Flavor flavor = convertMapToFlavor(flavorMap);
        final List<Scent> scents = getValidScentsOrElseThrow(alcoholTypeId, request.scentIds());

        // 3. 주관 평가 작성
        final TastingNote tastingNote = createBy(loginMember, alcoholType, alcoholicDrinks, color, alcoholicDrinksInfo, sensory, flavor, request);
        final List<TastingNoteScent> tastingNoteScents = TastingNoteScent.of(tastingNote, scents);
        final TastingNote result = tastingNoteWriter.create(tastingNote, tastingNoteScents);
        return TastingNoteWriteResponse.fromEntity(result);
    }

    private Color getValidColorOrElseThrow(final Long alcoholTypeId, final Long colorId) {
        final List<Color> colors = alcoholTypeColorReader.getAllColorByAlcoholTypeId(alcoholTypeId);
        return colors.stream()
                .filter(c -> Objects.equals(c.getId(), colorId))
                .findFirst()
                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_COLOR));
    }

    private List<Scent> getValidScentsOrElseThrow(final Long alcoholTypeId, final List<Long> scentIds) {
        final List<Scent> scents = alcoholTypeScentReader.getAllScentByAlcoholTypeId(alcoholTypeId);
        final Map<Long, Scent> scentMap = scents.stream()
                .collect(Collectors.toMap(Scent::getId, scent -> scent));
        return scentIds.stream()
                .map(scentId ->
                        Optional.ofNullable(scentMap.get(scentId))
                                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_SCENT))
                )
                .toList();
    }

    private void validateSensoryType(final Long alcoholTypeId, final Map<SensoryType, String> sensoryMap) {
        final List<SensoryType> sensoryTypes = alcoholTypeSensoryReader.getAllSensoryTypesByAlcoholTypeId(alcoholTypeId);
        final Set<SensoryType> sensoryTypesByAlcoholType = new HashSet<>(sensoryTypes);
        final Set<SensoryType> sensoryTypesByRequest = sensoryMap.keySet();
        if (!sensoryTypesByAlcoholType.equals(sensoryTypesByRequest)) {
            throw new InvalidParamException(ErrorCode.MISSING_SENSORY_TYPE);
        }
    }

    private void validateFlavorType(final Map<FlavorType, String> flavorMap) {
        final Set<FlavorType> flavorTypes = EnumSet.allOf(FlavorType.class);
        final Set<FlavorType> flavorTypesByRequest = flavorMap.keySet();
        if (!flavorTypesByRequest.equals(flavorTypes)) {
            throw new InvalidParamException(ErrorCode.MISSING_FLAVOR_TYPE);
        }
    }

    private Sensory convertMapToSensory(final Map<SensoryType, String> sensoryMap) {
        Sensory.SensoryBuilder sensoryBuilder = Sensory.builder();
        for (Map.Entry<SensoryType, String> entry : sensoryMap.entrySet()) {
            SensoryType sensoryType = entry.getKey();
            String levelName = entry.getValue();

            Rateable level = sensoryLevelFactory.getRateableBySensoryTypeAndLevel(sensoryType, levelName);
            sensoryLevelFactory.updateSensoryLevel(sensoryBuilder, sensoryType, level);
        }

        return sensoryBuilder.build();
    }

    private Flavor convertMapToFlavor(final Map<FlavorType, String> flavorMap) {
        Flavor.FlavorBuilder flavorBuilder = Flavor.builder();
        for (Map.Entry<FlavorType, String> entry : flavorMap.entrySet()) {
            FlavorType flavorType = entry.getKey();
            String levelName = entry.getValue();

            Rateable level = flavorLevelFactory.getRateableByFlavorTypeAndLevel(flavorType, levelName);
            flavorLevelFactory.updateFlavorLevel(flavorBuilder, flavorType, level);
        }

        return flavorBuilder.build();
    }

    private TastingNote createBy(final Member member,
                                 final AlcoholType alcoholType,
                                 final AlcoholicDrinks alcoholicDrinks,
                                 final Color color,
                                 final AlcoholicDrinksSnapshot alcoholicDrinksInfo,
                                 final Sensory sensory,
                                 final Flavor flavor,
                                 final TastingNoteWriteRequest request) {
        return TastingNote.of(
                member,
                alcoholType,
                alcoholicDrinks,
                color,
                alcoholicDrinksInfo,
                sensory,
                flavor,
                request.rating(),
                request.content(),
                request.isPrivate()
        );
    }

}

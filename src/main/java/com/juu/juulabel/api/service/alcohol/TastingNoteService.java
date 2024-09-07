package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.request.TastingNoteWriteRequest;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.factory.SliceResponseFactory;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.*;
import com.juu.juulabel.domain.embedded.AlcoholicDrinksSnapshot;
import com.juu.juulabel.domain.entity.alcohol.FlavorLevel;
import com.juu.juulabel.domain.entity.alcohol.*;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.reader.*;
import com.juu.juulabel.domain.repository.writer.TastingNoteWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    private final AlcoholTypeFlavorReader alcoholTypeFlavorReader;
    private final AlcoholTypeSensoryReader alcoholTypeSensoryReader;
    private final AlcoholDrinksTypeReader alcoholDrinksTypeReader;

    private final TastingNoteWriter tastingNoteWriter;

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
    public AlcoholicCategoryResponse searchAlcoholTypeList(final Member member, final CategorySearchAlcoholRequest request) {

        final Slice<AlcoholSearchSummary> alcoholicDrinks = alcoholDrinksTypeReader.getAlcoholicDrinksByType(member, request.type(),request.pageSize());

        // 검색된 전체 갯수 가져오기
        long totalCount = alcoholDrinksTypeReader.countByAlcoholType(request.type());

//        return SliceResponseFactory.create(
//                AlcoholicCategoryResponse.class,
//                alcoholicDrinks.isLast(),
//                alcoholicDrinks.getContent(),
//                totalCount
//        );
        return new AlcoholicCategoryResponse(
                alcoholicDrinks.isLast(),
                totalCount,
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
        final List<SensoryLevelInfo> sensoryLevels = alcoholTypeSensoryReader.getAllSensoryLevelInfoByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteSensoryListResponse(sensoryLevels);
    }

    @Transactional(readOnly = true)
    public TastingNoteScentListResponse loadTastingNoteScentList(final Long alcoholTypeId) {
        final List<CategoryWithScentSummary> categories = alcoholTypeScentReader.getAllCategoryWithScentByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteScentListResponse(categories);
    }

    @Transactional(readOnly = true)
    public TastingNoteFlavorListResponse loadTastingNoteFlavorList(final Long alcoholTypeId) {
        final List<FlavorLevelInfo> flavorLevels = alcoholTypeFlavorReader.getAllFlavorLevelInfoByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteFlavorListResponse(flavorLevels);
    }

    @Transactional
    public TastingNoteWriteResponse write(final Member loginMember, final TastingNoteWriteRequest request) {
        // 1. 입력된 주종 확인
        final Long alcoholTypeId = request.alcoholTypeId();
        final AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);

        // 2. 전통주 정보 확인 (OD, UD)
        final AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getByIdOrElseNull(request.alcoholicDrinksId());
        final AlcoholicDrinksSnapshot alcoholicDrinksInfo = AlcoholicDrinksSnapshot.fromDto(request.alcoholicDrinksDetails());

        // 3. 감각 정보 확인 (시각 정보, 촉각 정보, 미각 정보, 후각 정보)
        final Color color = getValidColorOrElseThrow(alcoholTypeId, request.colorId());
        final List<Scent> scents = getValidScentsOrElseThrow(alcoholTypeId, request.scentIds());
        final List<FlavorLevel> flavorLevels = getValidFlavorLevelsOrElseThrow(alcoholTypeId, request.flavorLevelIds());
        final List<SensoryLevel> sensoryLevels = getValidSensoryLevelsOrElseThrow(alcoholTypeId, request.sensoryLevelIds());

        // 4. 시음 노트 정보 생성 (작성)
        final TastingNote tastingNote = createBy(loginMember, alcoholType, alcoholicDrinks, color, alcoholicDrinksInfo, request);
        final TastingNote result = tastingNoteWriter.create(
                tastingNote,
                TastingNoteScent.of(tastingNote, scents),
                TastingNoteFlavorLevel.of(tastingNote, flavorLevels),
                TastingNoteSensoryLevel.of(tastingNote, sensoryLevels));

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

    private List<SensoryLevel> getValidSensoryLevelsOrElseThrow(final Long alcoholTypeId, final List<Long> sensoryLevelIds) {
        final List<SensoryLevel> sensoryLevels = alcoholTypeSensoryReader.getAllSensoryLevelByAlcoholTypeId(alcoholTypeId);
        final Map<Long, SensoryLevel> sensoryLevelMap = sensoryLevels.stream()
                .collect(Collectors.toMap(SensoryLevel::getId, sensoryLevel -> sensoryLevel));
        return sensoryLevelIds.stream()
                .map(sensoryLevelId ->
                        Optional.ofNullable(sensoryLevelMap.get(sensoryLevelId))
                                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_SENSORY))
                )
                .toList();
    }

    private List<FlavorLevel> getValidFlavorLevelsOrElseThrow(final Long alcoholTypeId, final List<Long> flavorLevelIds) {
        final List<FlavorLevel> flavorLevels = alcoholTypeFlavorReader.getAllFlavorLevelByAlcoholTypeId(alcoholTypeId);
        final Map<Long, FlavorLevel> flavorMap = flavorLevels.stream()
                .collect(Collectors.toMap(FlavorLevel::getId, flavorLevel -> flavorLevel));
        return flavorLevelIds.stream()
                .map(flavorLevelId ->
                        Optional.ofNullable(flavorMap.get(flavorLevelId))
                                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_FLAVOR))
                )
                .toList();
    }

    private TastingNote createBy(final Member member,
                                 final AlcoholType alcoholType,
                                 final AlcoholicDrinks alcoholicDrinks,
                                 final Color color,
                                 final AlcoholicDrinksSnapshot alcoholicDrinksInfo,
                                 final TastingNoteWriteRequest request) {
        return TastingNote.of(
                member,
                alcoholType,
                alcoholicDrinks,
                color,
                alcoholicDrinksInfo,
                request.rating(),
                request.content(),
                request.isPrivate()
        );
    }

}

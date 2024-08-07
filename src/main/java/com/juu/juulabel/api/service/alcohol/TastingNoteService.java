package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.request.TastingNoteWriteRequest;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.factory.FlavorLevelFactory;
import com.juu.juulabel.api.factory.SensoryLevelFactory;
import com.juu.juulabel.api.factory.SliceResponseFactory;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TastingNoteService {

    private final ColorReader colorReader;
    private final ScentReader scentReader;
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
        final List<ColorInfo> colors = alcoholTypeColorReader.getAllByAlcoholTypeId(alcoholTypeId);
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
        // 1. 전통주 정보 입력
        final AlcoholType alcoholType = alcoholTypeReader.getById(request.alcoholTypeId());
        final AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getByIdOrElseNull(request.alcoholicDrinksId());
        final AlcoholicDrinksSnapshot alcoholicDrinksInfo = AlcoholicDrinksSnapshot.of(request.alcoholicDrinksDetails());

        // 2. 감각 정보 입력
        final Color color = colorReader.getById(request.colorId());
        final List<Scent> scents = scentReader.getAllByIds(request.scentIds());
        final Sensory sensory = convertMapToSensory(request.sensoryMap());
        final Flavor flavor = convertMapToFlavor(request.flavorMap());

        // 3. 시음노트 작성
        final TastingNote tastingNote = TastingNote.of(alcoholType,
                alcoholicDrinks,
                color,
                alcoholicDrinksInfo,
                sensory,
                flavor,
                request.rating(),
                request.content(),
                request.isPrivate()
        );
        final List<TastingNoteScent> tastingNoteScents = TastingNoteScent.of(tastingNote, scents);
        final TastingNote result = tastingNoteWriter.create(tastingNote, tastingNoteScents);
        return TastingNoteWriteResponse.fromEntity(result);
    }

    private Sensory convertMapToSensory(Map<SensoryType, String> sensoryMap) {
        Sensory.SensoryBuilder sensoryBuilder = Sensory.builder();
        for (Map.Entry<SensoryType, String> entry : sensoryMap.entrySet()) {
            SensoryType sensoryType = entry.getKey();
            String levelName = entry.getValue();

            Rateable level = sensoryLevelFactory.getRateableBySensoryTypeAndLevel(sensoryType, levelName);
            sensoryLevelFactory.updateSensoryLevel(sensoryBuilder, sensoryType, level);
        }

        return sensoryBuilder.build();
    }

    private Flavor convertMapToFlavor(Map<FlavorType, String> flavorMap) {
        Flavor.FlavorBuilder flavorBuilder = Flavor.builder();
        for (Map.Entry<FlavorType, String> entry : flavorMap.entrySet()) {
            FlavorType flavorType = entry.getKey();
            String levelName = entry.getValue();

            Rateable level = flavorLevelFactory.getRateableByFlavorTypeAndLevel(flavorType, levelName);
            flavorLevelFactory.updateFlavorLevel(flavorBuilder, flavorType, level);
        }

        return flavorBuilder.build();
    }

}

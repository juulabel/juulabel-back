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
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.Color;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.enums.Rateable;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorType;
import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import com.juu.juulabel.domain.repository.reader.*;
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

    private final SensoryLevelFactory sensoryLevelFactory;
    private final FlavorLevelFactory flavorLevelFactory;

    private final ColorReader colorReader;
    private final TastingNoteReader tastingNoteReader;
    private final AlcoholTypeReader alcoholTypeReader;
    private final AlcoholicDrinksReader alcoholicDrinksReader;
    private final AlcoholTypeColorReader alcoholTypeColorReader;
    private final AlcoholTypeScentReader alcoholTypeScentReader;
    private final AlcoholTypeSensoryReader alcoholTypeSensoryReader;


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
        final AlcoholType alcoholType = alcoholTypeReader.getById(request.alcoholTypeId());
        final AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.findById(request.alcoholicDrinksId());
        final AlcoholicDrinksSnapshot alcoholicDrinksInfo = AlcoholicDrinksSnapshot.official(request.alcoholicDrinksDetails());
        final Color color = colorReader.getById(request.colorId());
        final Sensory sensory = convertMapToSensory(request.sensoryMap());
        final Flavor flavor = convertMapToFlavor(request.flavorMap());
        return new TastingNoteWriteResponse(null);
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

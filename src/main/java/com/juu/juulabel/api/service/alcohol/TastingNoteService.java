package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.response.AlcoholDrinksListResponse;
import com.juu.juulabel.api.dto.response.TastingNoteColorListResponse;
import com.juu.juulabel.api.dto.response.TastingNoteFlavorListResponse;
import com.juu.juulabel.api.dto.response.TastingNoteSensoryListResponse;
import com.juu.juulabel.api.factory.FlavorLevelFactory;
import com.juu.juulabel.api.factory.SensoryLevelFactory;
import com.juu.juulabel.api.factory.SliceResponseFactory;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.alcohol.ColorInfo;
import com.juu.juulabel.domain.dto.alcohol.FlavorLevel;
import com.juu.juulabel.domain.dto.alcohol.SensoryLevel;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorType;
import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import com.juu.juulabel.domain.repository.reader.AlcoholTypeColorReader;
import com.juu.juulabel.domain.repository.reader.AlcoholTypeSensoryReader;
import com.juu.juulabel.domain.repository.reader.TastingNoteReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TastingNoteService {

    private final SensoryLevelFactory sensoryLevelFactory;
    private final FlavorLevelFactory flavorLevelFactory;

    private final TastingNoteReader tastingNoteReader;
    private final AlcoholTypeColorReader alcoholTypeColorReader;
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
    public TastingNoteFlavorListResponse loadTastingNoteFlavorList() {
        final List<FlavorType> flavorTypes = Arrays.asList(FlavorType.values());
        final List<FlavorLevel> flavorLevels = flavorLevelFactory.getAllFlavorLevel(flavorTypes);
        return new TastingNoteFlavorListResponse(flavorLevels);
    }

}

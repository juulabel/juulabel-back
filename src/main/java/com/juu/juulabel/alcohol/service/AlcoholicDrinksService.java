package com.juu.juulabel.alcohol.service;

import com.juu.juulabel.alcohol.domain.SortType;
import com.juu.juulabel.alcohol.repository.AlcoholicDrinksReader;
import com.juu.juulabel.alcohol.response.*;
import com.juu.juulabel.common.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.common.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.common.dto.response.RelationSearchResponse;
import com.juu.juulabel.tastingnote.request.LikeTopTastingNoteSummary;
import com.juu.juulabel.tastingnote.request.TastingNoteSensorSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlcoholicDrinksService {

    private final AlcoholicDrinksReader alcoholDrinksReader;

    // 전통주 상세보기
    @Transactional(readOnly = true)
    public AlcoholicDrinksDetailResponse loadAlcoholicDrinks(final long alcoholicId) {

        AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo = alcoholDrinksReader.getAlcoholDrinksDetailById(alcoholicId);

        List<IngredientSummary> ingredientSummaryList = alcoholDrinksReader.getAlcoholDrinksIngredients(alcoholicId);

        TastingNoteSensorSummary tastingNoteSensorSummary = alcoholDrinksReader.getTastingNoteSensor(alcoholicId);

        List<LikeTopTastingNoteSummary> tastingNoteSummary = alcoholDrinksReader.getTastingNote(alcoholicId);

        List<VolumePriceDetail> volumePriceDetails = alcoholDrinksReader.getVolumePriceDetails(alcoholicId);


        return new AlcoholicDrinksDetailResponse(
                alcoholicDrinksDetailInfo,
                volumePriceDetails,
                ingredientSummaryList,
                tastingNoteSensorSummary,
                tastingNoteSummary
        );
    }

    // 주종별 검색 시 정렬
    @Transactional(readOnly = true)
    public AlcoholicCategoryResponse loadAlcoholDrinksList(final CategorySearchAlcoholRequest request) {

        // arrayType 이 null 이면 기본 정렬 "name" == 가나다순
        SortType sortType = request.sortType() == null ? SortType.NAME: request.sortType();

        final Slice<AlcoholSearchSummary> alcoholicDrinks = alcoholDrinksReader.getAlcoholicDrinksByType(request.type(),request.lastAlcoholicDrinksName(), request.pageSize(), sortType);

        // 검색된 전체 갯수 가져오기
        long totalCount = alcoholDrinksReader.countByAlcoholType(request.type());

        return new AlcoholicCategoryResponse(
                alcoholicDrinks.isLast(),
                totalCount,
                alcoholicDrinks
        );
    }

    @Transactional
    public RelationSearchResponse loadRelatedSearch(final String keyword) {
        List<String> relatedSearch = alcoholDrinksReader.getRelatedSearch(keyword);
        return new RelationSearchResponse(relatedSearch);
    }

}

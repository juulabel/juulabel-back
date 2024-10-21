package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.api.dto.response.RelationSearchResponse;
import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.domain.dto.alcohol.IngredientSummary;
import com.juu.juulabel.domain.dto.tastingnote.LikeTopTastingNoteSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSensorSummary;
import com.juu.juulabel.domain.enums.sort.SortType;
import com.juu.juulabel.domain.repository.reader.AlcoholicDrinksReader;
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


        return new AlcoholicDrinksDetailResponse(
                alcoholicDrinksDetailInfo,
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

        final Slice<AlcoholSearchSummary> alcoholicDrinks = alcoholDrinksReader.getAlcoholicDrinksByType(request.type(), request.pageSize(), sortType);

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

package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.domain.repository.reader.AlcoholicDrinksReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlcoholicDrinksService {

    private final AlcoholicDrinksReader alcoholDrinksReader;

    // 전통주 상세보기
    @Transactional(readOnly = true)
    public AlcoholicDrinksDetailResponse loadAlcoholicDrinks(final long alcoholicId) {

      final AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo = alcoholDrinksReader.getAlcoholDrinksDetailById(alcoholicId);

         return new AlcoholicDrinksDetailResponse(
                alcoholicDrinksDetailInfo
         );
    }


    // 주종별 검색 시 정렬
    @Transactional(readOnly = true)
    public AlcoholicCategoryResponse searchAlcoholTypeList(final CategorySearchAlcoholRequest request) {

        // arrayType 이 null 이면 기본 정렬 "name" == 가나다순
        String arrayType = request.arrayType() == null || request.arrayType().isBlank() ? "name" :request.arrayType();

        final Slice<AlcoholSearchSummary> alcoholicDrinks = alcoholDrinksReader.getAlcoholicDrinksByType(request.type(),request.pageSize(), arrayType);

        // 검색된 전체 갯수 가져오기
        long totalCount = alcoholDrinksReader.countByAlcoholType(request.type());

        return new AlcoholicCategoryResponse(
                alcoholicDrinks.isLast(),
                totalCount,
                alcoholicDrinks.getContent()
        );
    }

}

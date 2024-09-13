package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.reader.AlcoholDrinksDetailReader;
import com.juu.juulabel.domain.repository.reader.AlcoholDrinksTypeReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlcoholicDrinksService {

    private final AlcoholDrinksDetailReader alcoholDrinksDetailReader;
    private final AlcoholDrinksTypeReader alcoholDrinksTypeReader;

    // 전통주 상세보기
    @Transactional(readOnly = true)
    public AlcoholicDrinksDetailResponse loadAlcoholicDrinks(final long alcoholicId) {

      final AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo = alcoholDrinksDetailReader.getAlcoholDrinksDetailById(alcoholicId);

         return new AlcoholicDrinksDetailResponse(
                alcoholicDrinksDetailInfo
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

}

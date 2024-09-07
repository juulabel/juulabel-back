package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
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

    // 전통주 상세보기
    @Transactional(readOnly = true)
    public AlcoholicDrinksDetailResponse loadAlcoholicDrinks(final long alcoholicId) {

      final AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo = alcoholDrinksDetailReader.getAlcoholDrinksDetailById(alcoholicId);

         return new AlcoholicDrinksDetailResponse(
                alcoholicDrinksDetailInfo
         );
    }
}

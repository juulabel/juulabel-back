package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.api.annotation.SliceResponse;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@SliceResponse(content = AlcoholicDrinksSummary.class)
@Schema(description = "전통주 리스트 조회 응답")
public record AlcoholDrinksListResponse(
        @Schema(description = "마지막 페이지 여부")
        boolean isLast,
        @Schema(description = "총 검색 결과 개수")
        long totalCount,
        @Schema(description = "전통주 간단 정보")
        List<AlcoholicDrinksSummary> alcoholicDrinks
) {
}

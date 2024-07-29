package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "전통주 리스트 조회 응답")
public record AlcoholDrinksListResponse(
        @Schema(description = "전통주 간단 정보")
        List<AlcoholicDrinksSummary> alcoholicDrinks
) {
}

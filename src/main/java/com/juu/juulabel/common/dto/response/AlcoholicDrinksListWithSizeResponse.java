package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.response.AlcoholicDrinksSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AlcoholicDrinksListWithSizeResponse(
    @Schema(description = "전통주 간단 정보")
    List<AlcoholicDrinksSummary> alcoholicDrinks
) {
}

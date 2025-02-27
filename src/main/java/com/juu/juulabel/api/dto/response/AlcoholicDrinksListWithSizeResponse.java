package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AlcoholicDrinksListWithSizeResponse(
    @Schema(description = "전통주 간단 정보")
    List<AlcoholicDrinksSummary> alcoholicDrinks
) {
}

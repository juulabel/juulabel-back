package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.response.AlcoholicDrinksSummary;
import org.springframework.data.domain.Slice;

public record MyAlcoholicDrinksListResponse(
    Slice<AlcoholicDrinksSummary> alcoholicDrinksSummaries
) {
}
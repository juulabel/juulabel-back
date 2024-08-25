package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import org.springframework.data.domain.Slice;

public record MyAlcoholicDrinksListResponse(
    Slice<AlcoholicDrinksSummary> alcoholicDrinksSummaries
) {
}
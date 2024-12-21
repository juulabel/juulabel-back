package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.tastingnote.AlcoholicDrinksTastingNoteSummary;
import org.springframework.data.domain.Slice;

public record TastingNoteListResponseForAlcoholicDrinks(
    Slice<AlcoholicDrinksTastingNoteSummary> tastingNoteSummaries
) {
}
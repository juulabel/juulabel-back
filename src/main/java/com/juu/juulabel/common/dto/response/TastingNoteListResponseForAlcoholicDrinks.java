package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.tastingnote.request.AlcoholicDrinksTastingNoteSummary;
import org.springframework.data.domain.Slice;

public record TastingNoteListResponseForAlcoholicDrinks(
    Slice<AlcoholicDrinksTastingNoteSummary> tastingNoteSummaries
) {
}
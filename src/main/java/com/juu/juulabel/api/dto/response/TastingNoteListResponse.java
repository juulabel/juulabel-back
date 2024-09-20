package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSummary;
import org.springframework.data.domain.Slice;

public record TastingNoteListResponse(
    Slice<TastingNoteSummary> tastingNoteSummaries
) {
}

package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.tastingnote.request.TastingNoteSummary;
import org.springframework.data.domain.Slice;

public record TastingNoteListResponse(
    Slice<TastingNoteSummary> tastingNoteSummaries
) {
}

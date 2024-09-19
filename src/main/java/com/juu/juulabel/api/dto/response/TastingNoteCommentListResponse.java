package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.tastingnote.TastingNoteCommentSummary;
import org.springframework.data.domain.Slice;

public record TastingNoteCommentListResponse(
    Slice<TastingNoteCommentSummary> tastingNoteCommentSummaries
) {
}

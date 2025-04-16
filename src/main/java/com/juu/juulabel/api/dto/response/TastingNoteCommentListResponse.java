package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.comment.CommentSummary;
import org.springframework.data.domain.Slice;

public record TastingNoteCommentListResponse(
    Slice<CommentSummary> tastingNoteCommentSummaries
) {
}

package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.comment.ReplySummary;
import org.springframework.data.domain.Slice;

public record TastingNoteReplyListResponse(
    Slice<ReplySummary> tastingNoteReplySummaries
) {
}

package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.common.dto.comment.CommentSummary;
import org.springframework.data.domain.Slice;

public record DailyLifeCommentListResponse(
    Slice<CommentSummary> dailyLifeCommentSummaries
) {
}
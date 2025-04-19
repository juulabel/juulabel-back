package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.common.dto.comment.ReplySummary;
import org.springframework.data.domain.Slice;

public record DailyLifeReplyListResponse(
    Slice<ReplySummary> dailyLifeReplySummaries
) {
}
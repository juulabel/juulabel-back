package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.dailylife.DailyLifeReplySummary;
import org.springframework.data.domain.Slice;

public record DailyLifeReplyListResponse(
    Slice<DailyLifeReplySummary> dailyLifeReplySummaries
) {
}
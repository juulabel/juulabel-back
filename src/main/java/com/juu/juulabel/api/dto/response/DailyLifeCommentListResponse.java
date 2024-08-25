package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.dailylife.DailyLifeCommentSummary;
import org.springframework.data.domain.Slice;

public record DailyLifeCommentListResponse(
    Slice<DailyLifeCommentSummary> dailyLifeCommentSummaries
) {
}
package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.dailylife.response.DailyLifeSummary;
import org.springframework.data.domain.Slice;

public record DailyLifeListResponse(
    Slice<DailyLifeSummary> dailyLifeSummaries
) {
}
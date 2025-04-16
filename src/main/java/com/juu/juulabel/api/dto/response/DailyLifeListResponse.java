package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.dailylife.DailyLifeSummary;
import org.springframework.data.domain.Slice;

public record DailyLifeListResponse(
    Slice<DailyLifeSummary> dailyLifeSummaries
) {
}
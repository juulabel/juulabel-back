package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.dailylife.MyDailyLifeSummary;
import org.springframework.data.domain.Slice;

public record MyDailyLifeListResponse(
    Slice<MyDailyLifeSummary> myDailyLifeSummaries
) {
}
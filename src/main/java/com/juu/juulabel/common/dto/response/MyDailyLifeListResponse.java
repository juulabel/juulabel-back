package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.dailylife.response.MyDailyLifeSummary;
import org.springframework.data.domain.Slice;

public record MyDailyLifeListResponse(
    Slice<MyDailyLifeSummary> myDailyLifeSummaries
) {
}
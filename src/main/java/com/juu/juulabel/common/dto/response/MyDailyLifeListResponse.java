package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.dailylife.request.MyDailyLifeSummary;
import org.springframework.data.domain.Slice;

public record MyDailyLifeListResponse(
    Slice<MyDailyLifeSummary> myDailyLifeSummaries
) {
}
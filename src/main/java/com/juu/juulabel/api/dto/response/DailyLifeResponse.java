package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeImageInfo;

public record DailyLifeResponse(
    DailyLifeDetailInfo dailyLifeDetailInfo,
    DailyLifeImageInfo dailyLifeImageInfo
) {
}
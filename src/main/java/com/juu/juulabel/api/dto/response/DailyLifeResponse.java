package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.ImageInfo;

public record DailyLifeResponse(
    DailyLifeDetailInfo dailyLifeDetailInfo,
    ImageInfo imageInfo
) {
}
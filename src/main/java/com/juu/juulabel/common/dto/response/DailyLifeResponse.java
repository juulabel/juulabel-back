package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.dailylife.response.DailyLifeDetailInfo;
import com.juu.juulabel.common.dto.ImageInfo;

public record DailyLifeResponse(
    DailyLifeDetailInfo dailyLifeDetailInfo,
    ImageInfo imageInfo
) {
}
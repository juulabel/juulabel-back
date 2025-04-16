package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.dailylife.request.DailyLifeDetailInfo;
import com.juu.juulabel.common.dto.ImageInfo;

public record DailyLifeResponse(
    DailyLifeDetailInfo dailyLifeDetailInfo,
    ImageInfo imageInfo
) {
}
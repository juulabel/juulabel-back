package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeImageInfo;
import com.juu.juulabel.domain.dto.member.MemberInfo;

public record LoadDailyLifeResponse(
    DailyLifeDetailInfo dailyLifeDetailInfo,
    DailyLifeImageInfo dailyLifeImageInfo,
    MemberInfo memberInfo
) {
}
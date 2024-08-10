package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.member.MemberInfo;

import java.util.List;

public record WriteDailyLifeResponse(
    String title,
    String content,
    Long dailyLifeId,
    MemberInfo memberInfo,
    List<String> imageUrlList,
    int imageCount
) {
}

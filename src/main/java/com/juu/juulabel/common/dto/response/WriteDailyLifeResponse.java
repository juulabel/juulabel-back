package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.member.request.MemberInfo;

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

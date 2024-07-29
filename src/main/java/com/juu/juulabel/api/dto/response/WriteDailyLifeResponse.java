package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.member.MemberInfo;

import java.util.List;

public record WriteDailyLifeResponse(
    Long dailyLifeId,
    MemberInfo memberInfo,
    String title,
    String content,
    boolean isPrivate,
    List<String> imageUrlList,
    int imageCount
) {
}

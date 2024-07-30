package com.juu.juulabel.domain.dto.dailylife;

import com.juu.juulabel.domain.dto.member.MemberInfo;

import java.time.LocalDateTime;

public record DailyLifeDetailInfo(
    String title,
    String content,
    Long dailyLifeId,
    MemberInfo memberInfo,
    LocalDateTime createdAt,
    long likeCount,
    long commentCount
) {
}

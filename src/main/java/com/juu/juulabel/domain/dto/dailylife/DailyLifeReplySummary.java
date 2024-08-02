package com.juu.juulabel.domain.dto.dailylife;

import com.juu.juulabel.domain.dto.member.MemberInfo;

import java.time.LocalDateTime;

public record DailyLifeReplySummary(
    String content,
    Long commentId,
    MemberInfo memberInfo,
    LocalDateTime createdAt,
    long likeCount,
    boolean isLiked
) {
}
package com.juu.juulabel.domain.dto.dailylife;

import com.juu.juulabel.domain.dto.member.MemberInfo;

import java.time.LocalDateTime;

public record DailyLifeSummary(
    String title,
    String content,
    Long dailyLifeId,
    MemberInfo memberInfo,
    String thumbnailPath,
    Long imageCount,
    LocalDateTime createdAt,
    long likeCount,
    long commentCount,
    boolean isLiked
) {
}
package com.juu.juulabel.domain.dto.dailylife;

import com.juu.juulabel.domain.dto.member.MemberInfo;

import java.time.LocalDateTime;

public record DailyLifeCommentSummary(
    String content,
    Long commentId,
    MemberInfo memberInfo,
    LocalDateTime createdAt,
    long likeCount,
    long replyCount,
    boolean isLiked
) {
}

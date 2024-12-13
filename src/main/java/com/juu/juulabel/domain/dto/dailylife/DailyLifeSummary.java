package com.juu.juulabel.domain.dto.dailylife;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juu.juulabel.domain.dto.member.MemberInfo;

import java.time.LocalDateTime;

public record DailyLifeSummary(
    String title,
    String content,
    Long dailyLifeId,
    MemberInfo memberInfo,
    String thumbnailPath,
    Long imageCount,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    LocalDateTime createdAt,
    long likeCount,
    long commentCount,
    boolean isLiked
) {
}
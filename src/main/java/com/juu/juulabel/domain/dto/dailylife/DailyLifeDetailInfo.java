package com.juu.juulabel.domain.dto.dailylife;

import java.time.LocalDateTime;

public record DailyLifeDetailInfo(
    String title,
    String content,
    Long dailyLifeId,
    LocalDateTime createdAt,
    long likeCount,
    long commentCount
) {
}

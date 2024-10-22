package com.juu.juulabel.domain.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juu.juulabel.domain.enums.notification.NotificationType;

import java.time.LocalDateTime;

public record NotificationSummary(
    Long id,
    String relatedUrl,
    String content,
    NotificationType notificationType,
    boolean isRead,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    LocalDateTime createdAt
) {
}

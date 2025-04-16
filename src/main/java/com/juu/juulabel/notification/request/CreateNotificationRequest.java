package com.juu.juulabel.notification.request;

import com.juu.juulabel.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateNotificationRequest(
    @NotNull
    NotificationType notificationType,
    @NotNull
    @Schema(description = "알림 내용")
    String content,
    @NotNull
    @Schema(description = "연관 Url")
    String url
) {
}

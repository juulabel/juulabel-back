package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.notification.NotificationSummary;
import org.springframework.data.domain.Slice;

public record NotificationListResponse(
    Slice<NotificationSummary> notificationSummaries
) {
}

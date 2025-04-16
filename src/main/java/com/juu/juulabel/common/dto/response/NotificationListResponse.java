package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.notification.response.NotificationSummary;
import org.springframework.data.domain.Slice;

public record NotificationListResponse(
    Slice<NotificationSummary> notificationSummaries
) {
}

package com.juu.juulabel.api.dto.request;

import java.util.List;

public record DeleteNotificationsRequest(
    List<Long> notificationIds
) {
}

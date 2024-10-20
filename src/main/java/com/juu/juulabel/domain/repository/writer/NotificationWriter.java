package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.notification.Notification;
import com.juu.juulabel.domain.repository.jpa.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class NotificationWriter {

    private final NotificationJpaRepository notificationJpaRepository;

    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

}

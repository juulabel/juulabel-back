package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.notification.Notification;
import com.juu.juulabel.domain.repository.jpa.NotificationJpaRepository;
import com.juu.juulabel.domain.repository.query.NotificationQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Writer
@RequiredArgsConstructor
public class NotificationWriter {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationQueryRepository notificationQueryRepository;

    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    public void deleteNotifications(Member member, List<Long> notificationIds) {
        notificationQueryRepository.deleteNotifications(member, notificationIds);
    }
}

package com.juu.juulabel.notification.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.notification.request.NotificationSummary;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.notification.repository.query.NotificationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class NotificationReader {

    private final NotificationQueryRepository notificationQueryRepository;

    public Slice<NotificationSummary> getAllByMemberId(
        Long memberId, Long lastNotificationId, int pageSize) {
        return notificationQueryRepository.getAllByMemberId(memberId, lastNotificationId, pageSize);
    }

    public void setNotificationsAsRead(Member member, Long notificationId) {
        notificationQueryRepository.setNotificationsAsRead(member, notificationId);
    }

    public void setAllNotificationsAsRead(Member member) {
        notificationQueryRepository.setAllNotificationsAsRead(member);
    }
}

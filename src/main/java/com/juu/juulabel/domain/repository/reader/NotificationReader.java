package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.notification.NotificationSummary;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.query.NotificationQueryRepository;
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

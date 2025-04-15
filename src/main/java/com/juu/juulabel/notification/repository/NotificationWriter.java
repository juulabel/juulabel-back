package com.juu.juulabel.notification.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.notification.domain.Notification;
import com.juu.juulabel.notification.repository.jpa.NotificationJpaRepository;
import com.juu.juulabel.notification.repository.query.NotificationQueryRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class NotificationWriter {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationQueryRepository notificationQueryRepository;

    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    public void deleteNotification(Member member, Long notificationId) {
        notificationQueryRepository.deleteNotification(member, notificationId);
    }

    public void deleteAllByMember(Member member) {
        notificationQueryRepository.deleteAllByMember(member);
    }

    public void deleteByReceiverAndContentAndRelatedUrl(Member author, String content, String url) {
        notificationJpaRepository.deleteByReceiverAndContentAndRelatedUrl(author, content, url);
    }

    public void deleteByReceiverAndContentAndRelatedUrlAndCommentId(Member author, String content, String url, Long commentId) {
        notificationJpaRepository.deleteByReceiverAndContentAndRelatedUrlAndCommentId(author, content, url, commentId);
    }
}

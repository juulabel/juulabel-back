package com.juu.juulabel.notification.repository.jpa;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
    void deleteByReceiverAndContentAndRelatedUrl(Member author, String content, String url);

    void deleteByReceiverAndContentAndRelatedUrlAndCommentId(Member author, String content, String url, Long commentId);
}

package com.juu.juulabel.domain.entity.notification;

import com.juu.juulabel.domain.base.BaseCreatedTimeEntity;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.enums.notification.NotificationType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "notification"
)
public class Notification extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '알림 고유 번호'")
    private Long id;

    @Column(name = "comment_id", columnDefinition = "BIGINT UNSIGNED comment '댓글 고유 번호'")
    private Long commentId;

    @Column(name = "content", nullable = false, columnDefinition = "varchar(100) comment '내용'")
    private String content;

    @Column(name = "related_url", nullable = false, columnDefinition = "varchar(100) comment '연관 URL'")
    private String relatedUrl;

    @Column(name = "is_read", nullable = false, columnDefinition = "tinyint comment '읽음 여부'")
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, columnDefinition = "varchar(20) comment '알림 타입'")
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member receiver;

    @Column(name = "profile_image_url", columnDefinition = "varchar(500) comment '프로필 이미지 URL'")
    private String profileImageUrl;

    public static Notification create(Member receiver, NotificationType notificationType, String content, String relatedUrl) {
        return Notification.builder()
            .receiver(receiver)
            .notificationType(notificationType)
            .content(content)
            .relatedUrl(relatedUrl)
            .isRead(false)
            .build();
    }

    public static Notification createWithCommentId(Member receiver,
                                                   NotificationType notificationType,
                                                   String content,
                                                   String relatedUrl,
                                                   Long commentId) {
        return Notification.builder()
            .commentId(commentId)
            .receiver(receiver)
            .notificationType(notificationType)
            .content(content)
            .relatedUrl(relatedUrl)
            .isRead(false)
            .build();
    }
}

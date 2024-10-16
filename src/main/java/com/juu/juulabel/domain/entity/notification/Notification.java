package com.juu.juulabel.domain.entity.notification;

import com.juu.juulabel.domain.base.BaseTimeEntity;
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
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '알림 고유 번호'")
    private Long id;

    private String title;

    private String content;

    private String url;

    @Column(nullable = false, name = "is_read")
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "notification_type")
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;
}

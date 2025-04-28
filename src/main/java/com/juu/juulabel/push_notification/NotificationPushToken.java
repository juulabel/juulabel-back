package com.juu.juulabel.push_notification;


import com.juu.juulabel.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "notification_push_token"
)
public class NotificationPushToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "comment '푸시 토큰 고유 번호'")
    private Long id;

    @Column(name = "member_id", nullable = false, columnDefinition = "comment '회원 고유 번호'")
    private Long memberId;

    @Column(name = "push_token", columnDefinition = "comment '푸시 토큰'")
    private String pushToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", columnDefinition = "comment '플랫폼'")
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_status", columnDefinition = "comment '푸시 알림 권한 상태'")
    private PermissionStatus permissionStatus;

    public String getPushToken() {
        return String.format("ExponentPushToken[%s]", pushToken);
    }
}



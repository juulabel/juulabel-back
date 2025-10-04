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
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "push_token")
    private String pushToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_status")
    private PermissionStatus permissionStatus;

    public String getPushToken() {
        return String.format("ExponentPushToken[%s]", pushToken);
    }
}



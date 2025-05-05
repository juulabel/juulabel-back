package com.juu.juulabel.member.domain;

import java.time.LocalDateTime;

import com.juu.juulabel.common.base.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_token_token_unq", columnList = "token", unique = true),
        @Index(name = "idx_refresh_token_member_id", columnList = "member_id"),
        @Index(name = "idx_refresh_token_expiry_date", columnList = "expires_at")
})
public class RefreshToken extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '리프레시 토큰 고유 번호'")
    private Long id;

    @Column(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Long memberId;

    @Column(name = "token", nullable = false, unique = true, columnDefinition = "varchar(255) comment '리프레시 토큰'")
    private String token;

    @Column(name = "parent_token_id", columnDefinition = "BIGINT UNSIGNED comment '부모 토큰 아이디'")
    private Long parentTokenId;

    @Column(name = "ip_address", nullable = false, columnDefinition = "varchar(255) comment 'IP 주소'")
    private String ipAddress;

    @Column(name = "user_agent", nullable = false, columnDefinition = "varchar(255) comment '유저 에이전트'")
    private String userAgent;

    @Column(name = "device_id", columnDefinition = "varchar(255) comment '디바이스 아이디'")
    private String deviceId;

    @Column(name = "expires_at", nullable = false, columnDefinition = "datetime comment '토큰 만료 일시'")
    private LocalDateTime expiresAt;

    @Column(name = "revoked", nullable = false, columnDefinition = "TINYINT(1) comment '토큰 무효화 여부'")
    @Builder.Default
    private boolean revoked = false;

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public void setParentTokenId(Long parentTokenId) {
        this.parentTokenId = parentTokenId;
    }
}
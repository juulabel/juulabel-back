package com.juu.juulabel.member.token;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
// Remove timeToLive from @RedisHash since we'll use @TimeToLive field
@RedisHash(value = "user_session")
public class UserSession implements Serializable {

    @Id
    private String id;

    @Indexed
    private Long memberId;

    private String email;

    private MemberRole role;

    private String deviceId;

    private String ipAddress;

    private String userAgent;

    private LocalDateTime createdAt;

    private LocalDateTime lastAccessedAt;

    @TimeToLive
    private Long ttl;

    /**
     * Factory method to create a UserSession from Member data
     * This replaces the business logic that was in the constructor
     */
    public static UserSession createFromMember(String sessionId, Member member,
            String deviceId, String ipAddress, String userAgent) {
        final LocalDateTime now = LocalDateTime.now();

        return UserSession.builder()
                .id(sessionId)
                .memberId(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .deviceId(deviceId)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .createdAt(now)
                .lastAccessedAt(now)
                .ttl((long) AuthConstants.USER_SESSION_TTL)
                .build();
    }

    /**
     * Creates a copy of this session with updated last accessed time
     * This replaces the business logic that was in updateLastAccessed method
     */
    public UserSession withUpdatedLastAccessed() {
        return UserSession.builder()
                .id(this.id)
                .memberId(this.memberId)
                .email(this.email)
                .role(this.role)
                .deviceId(this.deviceId)
                .ipAddress(this.ipAddress)
                .userAgent(this.userAgent)
                .createdAt(this.createdAt)
                .lastAccessedAt(LocalDateTime.now())
                .ttl((long) AuthConstants.USER_SESSION_TTL) // Reset TTL
                .build();
    }
}
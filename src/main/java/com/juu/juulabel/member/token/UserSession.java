package com.juu.juulabel.member.token;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.util.HttpRequestUtil;
import com.juu.juulabel.common.util.IpAddressExtractor;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Builder
    public UserSession(String id, Member member) {
        final LocalDateTime now = LocalDateTime.now();
        this.id = id;
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.deviceId = HttpRequestUtil.getDeviceId();
        this.ipAddress = IpAddressExtractor.getClientIpAddress();
        this.userAgent = HttpRequestUtil.getUserAgent();
        this.createdAt = now;
        this.lastAccessedAt = now;
        this.ttl = (long) AuthConstants.USER_SESSION_TTL; // 7 days in seconds
    }

    public void updateLastAccessed() {
        this.lastAccessedAt = LocalDateTime.now();
        this.ttl = (long) AuthConstants.USER_SESSION_TTL; // Reset TTL to original value
    }
}
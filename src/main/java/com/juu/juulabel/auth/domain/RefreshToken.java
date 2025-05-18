package com.juu.juulabel.auth.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_TTL_IN_SECONDS;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("RefreshToken") // Acts like a key prefix
public class RefreshToken implements Serializable {

    @Id
    private String hashedToken;

    @Indexed
    private Long memberId;

    @Indexed
    private String deviceId;

    private ClientId clientId;

    private Instant revokedAt;

    private Instant issuedAt;

    // Metadata
    // • ipAddress: Current request IP address
    // • userAgent: Current request user agent
    // • ttl: Time to live

    private String ipAddress;

    private String userAgent;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    @Builder
    public RefreshToken(String token, Long memberId, ClientId clientId, String deviceId, String ipAddress,
            String userAgent) {
        this.hashedToken = hashToken(token);
        this.memberId = memberId;
        this.clientId = clientId;
        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.revokedAt = null;
        this.issuedAt = Instant.now();
        this.ttl = REFRESH_TOKEN_TTL_IN_SECONDS;
    }

    public void setRevoked(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

}
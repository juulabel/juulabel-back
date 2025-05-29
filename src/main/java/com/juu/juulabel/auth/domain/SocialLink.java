package com.juu.juulabel.auth.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import static com.juu.juulabel.common.constants.AuthConstants.SOCIAL_LINK_DURATION;
import static com.juu.juulabel.common.constants.AuthConstants.SOCIAL_LINK_PREFIX;

import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Provider;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(SOCIAL_LINK_PREFIX)
public class SocialLink implements Serializable {

    @Id
    private String hashedEmail;

    private Provider provider;

    private String providerId;

    private String deviceId;

    private String ipAddress;

    private String userAgent;

    private Long usedAt;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    @Builder
    public SocialLink(String hashedEmail, Provider provider, String providerId, String deviceId, String userAgent,
            String ipAddress) {
        this.hashedEmail = hashedEmail;
        this.provider = provider;
        this.providerId = providerId;
        this.deviceId = deviceId;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.usedAt = null;
        this.ttl = SOCIAL_LINK_DURATION.getSeconds();
    }

    /**
     * Validates the social link against provided parameters for security purposes.
     * Throws AuthException if validation fails.
     */
    public void validate(Provider provider, String providerId, String deviceId, String userAgent) {
        // Check if already used
        if (isAlreadyUsed()) {
            throw new AuthException(ErrorCode.SOCIAL_LINK_ALREADY_USED);
        }

        // Validate parameters match stored values
        if (!isValidationParametersMatch(provider, providerId, deviceId, userAgent)) {
            throw new AuthException("Validation failed due to parameter mismatch");
        }
    }

    /**
     * Marks this social link as used with current timestamp.
     * Can only be used once.
     */
    public void markAsUsed() {
        if (isAlreadyUsed()) {
            throw new AuthException(ErrorCode.SOCIAL_LINK_ALREADY_USED);
        }
        this.usedAt = Instant.now().getEpochSecond();
    }

    /**
     * Checks if this social link has already been used.
     */
    public boolean isAlreadyUsed() {
        return this.usedAt != null;
    }

    /**
     * Checks if validation parameters match stored values.
     * Uses efficient short-circuit evaluation.
     */
    private boolean isValidationParametersMatch(Provider provider, String providerId, String deviceId,
            String userAgent) {
        return Objects.equals(this.provider, provider) &&
                Objects.equals(this.providerId, providerId) &&
                Objects.equals(this.deviceId, deviceId) &&
                Objects.equals(this.userAgent, userAgent);
    }
}

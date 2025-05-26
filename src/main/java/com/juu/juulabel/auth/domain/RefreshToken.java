package com.juu.juulabel.auth.domain;

import lombok.*;

import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_DURATION;
import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_HASH_PREFIX;
import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_INDEX_PREFIX;

import java.io.Serializable;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken implements Serializable {

    private String token;

    private String hashedToken;

    private Long memberId;

    private String deviceId;

    private ClientId clientId;

    private String ipAddress;

    private String userAgent;

    private Long ttl;

    private boolean revoked;

    @Builder
    public RefreshToken(String token, String hashedToken, Long memberId, ClientId clientId, String deviceId,
            String ipAddress, String userAgent) {
        this.token = token;
        this.hashedToken = hashedToken;
        this.memberId = memberId;
        this.clientId = clientId;
        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.ttl = REFRESH_TOKEN_DURATION.getSeconds();
        this.revoked = false;
    }

    public String getTokenKey() {
        return REFRESH_TOKEN_HASH_PREFIX + ":" + hashedToken;
    }

    public String getIndexKey() {
        return REFRESH_TOKEN_INDEX_PREFIX + ":" + memberId + ":" + clientId + ":" + deviceId;
    }

    public List<String> getArgs() {
        return List.of(memberId.toString(), clientId.toString(), deviceId, ipAddress, userAgent, ttl.toString());
    }

}
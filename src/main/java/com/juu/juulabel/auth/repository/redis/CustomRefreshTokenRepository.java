package com.juu.juulabel.auth.repository.redis;

import java.util.Optional;

import com.juu.juulabel.auth.domain.RefreshToken;

public interface CustomRefreshTokenRepository {

    void revokeByMemberId(Long memberId);

    void revokeByDeviceId(Long memberId, String deviceId);

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}

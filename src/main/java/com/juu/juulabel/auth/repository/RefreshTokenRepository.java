package com.juu.juulabel.auth.repository;

import com.juu.juulabel.auth.domain.ClientId;
import com.juu.juulabel.auth.domain.RefreshToken;

public interface RefreshTokenRepository {

    /**
     * Saves a refresh token
     */
    void save(RefreshToken refreshToken);

    /**
     * Rotate
     */
    void rotate(RefreshToken refreshToken, String hashedOldToken);

    /**
     * Login
     */
    void login(RefreshToken refreshToken);

    /**
     * Revokes all refresh tokens for a member and device
     */
    void revokeByMemberAndDevice(Long memberId, ClientId clientId, String deviceId);

    /**
     * Revokes all refresh tokens for a member
     */
    void revokeAllByMember(Long memberId);

}
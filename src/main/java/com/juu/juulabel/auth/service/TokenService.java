package com.juu.juulabel.auth.service;

import com.juu.juulabel.auth.domain.ClientId;
import com.juu.juulabel.auth.domain.RefreshToken;
import com.juu.juulabel.auth.repository.RefreshTokenRepository;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.properties.CookieProperties;
import com.juu.juulabel.common.provider.jwt.AccessTokenProvider;
import com.juu.juulabel.common.provider.jwt.RefreshTokenProvider;
import com.juu.juulabel.common.provider.jwt.SignupTokenProvider;
import com.juu.juulabel.common.util.CookieUtil;
import com.juu.juulabel.common.util.HttpRequestUtil;
import com.juu.juulabel.common.util.HashingUtil;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.request.OAuthUser;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing authentication tokens including access, refresh, and
 * signup tokens.
 * Handles token creation, rotation, revocation, and cookie management.
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final SignupTokenProvider signupTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieProperties cookieProperties;

    /**
     * Creates and sets tokens for member registration.
     * Clears any existing signup token upon successful registration.
     * 
     * @param member the member to create tokens for
     */
    @Transactional
    public String signUp(Member member) {
        return createAccessAndRefreshToken(member, refreshTokenRepository::save);
    }

    /**
     * Creates signup ready token for OAuth flow with enhanced validation.
     * 
     * @param oAuthUser the OAuth user information
     * @param nonce     the security nonce
     */
    public String createSignUpReadyToken(OAuthUser oAuthUser, String nonce) {
        return signupTokenProvider.createToken(oAuthUser, nonce);
    }

    /**
     * Creates tokens for login and manages device-specific token rotation.
     * 
     * @param member the member to create tokens for
     */
    @Transactional
    public String login(Member member) {
        return createAccessAndRefreshToken(member, refreshTokenRepository::login);
    }

    /**
     * Rotates refresh token for enhanced security.
     * Implements secure token rotation to prevent token replay attacks.
     * 
     * @param oldToken the current refresh token to rotate
     */
    @Transactional
    public String rotate(String oldToken) {

        final Member member = refreshTokenProvider.getMemberFromToken(oldToken);
        final String hashedOldToken = HashingUtil.hashSha256(oldToken);
        final RefreshToken newRefreshToken = refreshTokenProvider.buildRefreshToken(member);

        refreshTokenRepository.rotate(newRefreshToken, hashedOldToken);
        CookieUtil.addCookie(AuthConstants.REFRESH_TOKEN_NAME, newRefreshToken.getToken(),
                (int) AuthConstants.REFRESH_TOKEN_DURATION.getSeconds(), cookieProperties.isSecure());

        return accessTokenProvider.createToken(member);
    }

    /**
     * Revokes refresh token for logout with device-specific cleanup.
     * 
     * @param memberId the member ID for logout
     */
    @Transactional
    public void logout(Long memberId) {
        final String deviceId = HttpRequestUtil.getDeviceId();
        refreshTokenRepository.revokeByMemberAndDevice(memberId, ClientId.WEB, deviceId);
        CookieUtil.removeCookie(AuthConstants.REFRESH_TOKEN_NAME);
    }

    /**
     * Revokes all refresh tokens for account withdrawal.
     * Performs complete token cleanup for account deletion.
     * 
     * @param memberId the member ID for account withdrawal
     */
    @Transactional
    public void withdraw(Long memberId) {
        refreshTokenRepository.revokeAllByMember(memberId);
        CookieUtil.removeCookie(AuthConstants.REFRESH_TOKEN_NAME);
    }

    /**
     * Common method for creating and setting tokens with different repository
     * operations.
     * Centralizes token creation logic to reduce code duplication.
     * 
     * @param member              the member to create tokens for
     * @param repositoryOperation the repository operation to perform
     */
    private String createAccessAndRefreshToken(Member member, RepositoryOperation repositoryOperation) {

        final RefreshToken refreshToken = refreshTokenProvider.buildRefreshToken(member);

        repositoryOperation.execute(refreshToken);

        CookieUtil.addCookie(AuthConstants.REFRESH_TOKEN_NAME, refreshToken.getToken(),
                (int) AuthConstants.REFRESH_TOKEN_DURATION.getSeconds(), cookieProperties.isSecure());
        return accessTokenProvider.createToken(member);

    }

    /**
     * Functional interface for repository operations.
     * Enables flexible repository operation handling.
     */
    @FunctionalInterface
    private interface RepositoryOperation {
        void execute(RefreshToken refreshToken);
    }
}
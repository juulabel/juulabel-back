package com.juu.juulabel.auth.service;

import com.juu.juulabel.auth.domain.ClientId;
import com.juu.juulabel.auth.domain.RefreshToken;
import com.juu.juulabel.auth.repository.RefreshTokenRepository;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.provider.JwtTokenProvider;
import com.juu.juulabel.common.util.DeviceIdExtractor;
import com.juu.juulabel.common.util.IpAddressExtractor;
import com.juu.juulabel.common.util.UserAgentExtractor;
import com.juu.juulabel.common.util.HttpResponseUtil;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.token.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Creates access and refresh tokens for a member
     */
    @Transactional
    public Token createTokenPair(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member);
        RefreshToken refreshToken = createRefreshToken(member);

        refreshTokenRepository.save(refreshToken);
        setRefreshTokenCookie(refreshToken.getToken());

        return new Token(accessToken, jwtTokenProvider.getExpirationByToken(accessToken));
    }

    /**
     * Creates access token only (for existing members during login)
     */
    public Optional<Token> createAccessToken(Optional<Member> memberOpt) {
        return memberOpt.map(member -> {
            String accessToken = jwtTokenProvider.createAccessToken(member);
            return new Token(accessToken, jwtTokenProvider.getExpirationByToken(accessToken));
        });
    }

    /**
     * Creates refresh token for login (revokes existing tokens for same device)
     */
    @Transactional
    public void createLoginRefreshToken(Member member) {

        RefreshToken refreshToken = createRefreshToken(member);

        refreshTokenRepository.login(refreshToken);
        setRefreshTokenCookie(refreshToken.getToken());
    }

    /**
     * Rotates refresh token
     */
    @Transactional
    public Token rotateRefreshToken(String oldToken) {
        Member member = jwtTokenProvider.getMemberFromToken(oldToken);
        String hashedOldToken = jwtTokenProvider.hashToken(oldToken);

        RefreshToken newRefreshToken = createRefreshToken(member);

        refreshTokenRepository.rotate(newRefreshToken, hashedOldToken);

        setRefreshTokenCookie(newRefreshToken.getToken());

        String newAccessToken = jwtTokenProvider.createAccessToken(member);
        return new Token(newAccessToken, jwtTokenProvider.getExpirationByToken(newAccessToken));
    }

    /**
     * Revokes refresh token (logout)
     */
    @Transactional
    public void revokeRefreshToken(String token) {
        Member member = jwtTokenProvider.getMemberFromToken(token);
        String deviceId = DeviceIdExtractor.getDeviceId();

        refreshTokenRepository.revokeByMemberAndDevice(member.getId(), ClientId.WEB, deviceId);
        clearRefreshTokenCookie();
    }

    /**
     * Revokes all refresh tokens for a member (account deletion)
     */
    @Transactional
    public void revokeAllRefreshTokens(String token) {
        Member member = jwtTokenProvider.getMemberFromToken(token);

        refreshTokenRepository.revokeAllByMember(member.getId());
        clearRefreshTokenCookie();
    }

    private RefreshToken createRefreshToken(Member member) {
        String token = jwtTokenProvider.createRefreshToken(member);
        String hashedToken = jwtTokenProvider.hashToken(token);

        return RefreshToken.builder()
                .token(token)
                .hashedToken(hashedToken)
                .memberId(member.getId())
                .clientId(ClientId.WEB)
                .deviceId(DeviceIdExtractor.getDeviceId())
                .ipAddress(IpAddressExtractor.getClientIpAddress())
                .userAgent(UserAgentExtractor.getUserAgent())
                .build();
    }

    private void setRefreshTokenCookie(String token) {
        HttpResponseUtil.addCookie(
                AuthConstants.REFRESH_TOKEN_HEADER_NAME,
                token,
                (int) AuthConstants.REFRESH_TOKEN_DURATION.getSeconds());
    }

    private void clearRefreshTokenCookie() {
        HttpResponseUtil.addCookie(AuthConstants.REFRESH_TOKEN_HEADER_NAME, "", 0);
    }
}
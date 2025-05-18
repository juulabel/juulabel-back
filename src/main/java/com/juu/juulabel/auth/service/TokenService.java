package com.juu.juulabel.auth.service;

import com.juu.juulabel.auth.domain.RefreshToken;
import com.juu.juulabel.auth.repository.redis.RefreshTokenRedisRepository;
import com.juu.juulabel.common.dto.response.RefreshResponse;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.provider.JwtTokenProvider;
import com.juu.juulabel.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    public RefreshResponse refresh(String refreshTokenCookie) {
        Member member = jwtTokenProvider.getMemberFromToken(refreshTokenCookie);
        RefreshToken oldToken = validateAndGetOldToken(refreshTokenCookie);

        jwtTokenProvider.rotateRefreshToken(oldToken);

        return new RefreshResponse(jwtTokenProvider.createAccessToken(member));
    }

    @Transactional
    public void logout(String refreshTokenCookie, Long memberId) {

        refreshTokenRedisRepository.findByTokenHash(refreshTokenCookie)
                .ifPresent(token -> {
                    token.setRevoked(Instant.now());
                    refreshTokenRedisRepository.save(token);
                });
    }

    @Transactional
    public void saveAndSetCookie(Long memberId, String parentTokenId) {
        RefreshToken newToken = jwtTokenProvider.createRefreshToken(memberId, parentTokenId);

        RefreshToken oldToken = validateAndGetOldToken(parentTokenId);
        oldToken.setRevoked(Instant.now());

        refreshTokenRedisRepository.save(newToken);
        refreshTokenRedisRepository.save(oldToken);
    }

    private RefreshToken validateAndGetOldToken(String tokenStr) {
        return refreshTokenRedisRepository.findByTokenHash(tokenStr)
                .orElseThrow(() -> new BaseException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }
}
package com.juu.juulabel.common.provider.token.paseto;

import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.util.CookieUtil;

import dev.paseto.jpaseto.Claims;

@Component
public class SignupTokenProvider extends PasetoTokenProvider {

    private static final String AUDIENCE_CLAIM = "user-signup-completion";
    private static final String EMAIL_CLAIM = "email";
    private static final String PROVIDER_CLAIM = "provider";
    private static final String PROVIDER_ID_CLAIM = "providerId";
    private static final String NONCE_CLAIM = "nonce";
    private static final String AUDIENCE_CLAIM_KEY = "aud";

    private final MemberReader memberReader;
    private final CookieUtil cookieUtil;

    public SignupTokenProvider(@Value("${spring.jwt.signup-key}") String secretKey, MemberReader memberReader,
            CookieUtil cookieUtil) {
        super(secretKey, AuthConstants.SIGN_UP_TOKEN_DURATION);
        this.memberReader = memberReader;
        this.cookieUtil = cookieUtil;
    }

    public void createToken(OAuthUser oAuthUser, String nonce) {
        String token = builder()
                .claim(EMAIL_CLAIM, oAuthUser.email())
                .claim(PROVIDER_CLAIM, oAuthUser.provider().name())
                .claim(PROVIDER_ID_CLAIM, oAuthUser.id())
                .claim(NONCE_CLAIM, nonce)
                .claim(AUDIENCE_CLAIM_KEY, AUDIENCE_CLAIM)
                .compact();
        cookieUtil.addCookie(AuthConstants.SIGN_UP_TOKEN_NAME, token,
                (int) AuthConstants.SIGN_UP_TOKEN_DURATION.toSeconds());
    }

    public Authentication getAuthentication(String token) {
        Member member = verifyToken(token);
        return new UsernamePasswordAuthenticationToken(member, null, Collections.emptyList());
    }

    public Member verifyToken(String token) {
        Claims claims = parseClaims(token);

        // Extract and validate all claims at once
        TokenClaims tokenClaims = extractTokenClaims(claims);

        // Validate audience first (fast check)
        if (!AUDIENCE_CLAIM.equals(tokenClaims.audience())) {
            throw new AuthException("Invalid token audience", ErrorCode.INVALID_AUTHENTICATION);
        }

        // Get member and validate
        Member member = memberReader.getByEmail(tokenClaims.email());
        validateMemberAgainstToken(member, tokenClaims);

        return member;
    }

    private TokenClaims extractTokenClaims(Claims claims) {
        try {
            return new TokenClaims(
                    getRequiredClaimAsString(claims, EMAIL_CLAIM),
                    Provider.valueOf(getRequiredClaimAsString(claims, PROVIDER_CLAIM)),
                    getRequiredClaimAsString(claims, PROVIDER_ID_CLAIM),
                    getRequiredClaimAsString(claims, NONCE_CLAIM),
                    getRequiredClaimAsString(claims, AUDIENCE_CLAIM_KEY));
        } catch (IllegalArgumentException e) {
            throw new AuthException("Invalid provider in token", ErrorCode.INVALID_AUTHENTICATION);
        }
    }

    private void validateMemberAgainstToken(Member member, TokenClaims tokenClaims) {
        // Check provider and provider ID
        if (member.getProvider() != tokenClaims.provider()) {
            throw new AuthException("Provider mismatch", ErrorCode.PROVIDER_ID_MISMATCH);
        }

        if (!member.getProviderId().equals(tokenClaims.providerId())) {
            throw new AuthException("Provider ID mismatch", ErrorCode.PROVIDER_ID_MISMATCH);
        }

        if (!member.getNickname().equals(tokenClaims.nonce())) {
            throw new AuthException("Token validation failed", ErrorCode.INVALID_AUTHENTICATION);
        }

        // Check member status
        if (member.getStatus() != MemberStatus.PENDING) {
            throw new AuthException("Member already completed signup", ErrorCode.INVALID_AUTHENTICATION);
        }
    }

    private String getRequiredClaimAsString(Claims claims, String claimName) {
        Object claimValue = claims.get(claimName);
        if (claimValue == null) {
            throw new AuthException("Missing required claim: " + claimName, ErrorCode.INVALID_AUTHENTICATION);
        }
        return claimValue.toString();
    }

    /**
     * Record to hold extracted token claims for better type safety and performance
     */
    private record TokenClaims(
            String email,
            Provider provider,
            String providerId,
            String nonce,
            String audience) {
    }
}

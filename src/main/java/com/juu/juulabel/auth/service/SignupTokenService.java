package com.juu.juulabel.auth.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.http.CookieService;
import com.juu.juulabel.common.provider.token.paseto.PasetoTokenService;
import com.juu.juulabel.common.provider.token.validator.SignupTokenClaims;
import com.juu.juulabel.common.provider.token.validator.SignupTokenValidator;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.request.OAuthUser;

import dev.paseto.jpaseto.Claims;

/**
 * Service for handling signup token operations with business logic.
 * Extends PasetoTokenService to inherit PASETO-specific token operations.
 */
@Service
public class SignupTokenService extends PasetoTokenService {

    private static final String AUDIENCE_CLAIM = "user-signup-completion";
    private static final String EMAIL_CLAIM = "email";
    private static final String PROVIDER_CLAIM = "provider";
    private static final String PROVIDER_ID_CLAIM = "providerId";
    private static final String NONCE_CLAIM = "nonce";
    private static final String AUDIENCE_CLAIM_KEY = "aud";

    private final SignupTokenValidator validator;
    private final MemberReader memberReader;
    private final CookieService cookieService;

    public SignupTokenService(
            @Value("${app.paseto.sign-up-key}") String secretKey,
            SignupTokenValidator validator,
            MemberReader memberReader,
            CookieService cookieService) {

        super(secretKey, AuthConstants.SIGN_UP_TOKEN_DURATION);
        this.validator = validator;
        this.memberReader = memberReader;
        this.cookieService = cookieService;
    }

    /**
     * Creates and sets signup token as HTTP-only cookie
     */
    public void createAndSetToken(OAuthUser oAuthUser, String nonce) {
        Map<String, Object> claims = Map.of(
                EMAIL_CLAIM, oAuthUser.email(),
                PROVIDER_CLAIM, oAuthUser.provider().name(),
                PROVIDER_ID_CLAIM, oAuthUser.id(),
                NONCE_CLAIM, nonce,
                AUDIENCE_CLAIM_KEY, AUDIENCE_CLAIM);

        String token = createToken(claims);
        cookieService.addCookie(
                AuthConstants.SIGN_UP_TOKEN_NAME,
                token,
                (int) AuthConstants.SIGN_UP_TOKEN_DURATION.toSeconds());
    }

    /**
     * Verifies token and returns authenticated member
     */
    public Authentication getAuthentication(String token) {
        Member member = verifyTokenAndGetMember(token);
        return new UsernamePasswordAuthenticationToken(member, null, Collections.emptyList());
    }

    /**
     * Verifies signup token and returns the associated member
     */
    public Member verifyTokenAndGetMember(String token) {
        // Parse token claims (inherited from PasetoTokenService)
        Claims claims = parseToken(token);

        // Convert to map and then to structured claims
        Map<String, Object> claimsMap = convertClaimsToMap(claims);
        SignupTokenClaims signupClaims = SignupTokenClaims.from(claimsMap);

        // Validate claims
        validator.validate(signupClaims);

        // Return validated member
        return memberReader.getByEmail(signupClaims.email());
    }

    /**
     * Resolves token from header by removing token prefix
     * This method maintains compatibility with the existing TokenProvider interface
     */
    public String resolveToken(String header) {
        if (!org.springframework.util.StringUtils.hasText(header)) {
            throw new com.juu.juulabel.common.exception.InvalidParamException(
                    com.juu.juulabel.common.exception.code.ErrorCode.INVALID_AUTHENTICATION);
        }
        return header.replace(AuthConstants.TOKEN_PREFIX, "");
    }

    /**
     * Creates signup token and sets it as cookie
     * This method name maintains compatibility with the existing
     * SignupTokenProvider
     */
    public void createToken(OAuthUser oAuthUser, String nonce) {
        createAndSetToken(oAuthUser, nonce);
    }

    /**
     * Converts PASETO Claims to Map for easier processing
     */
    private Map<String, Object> convertClaimsToMap(Claims claims) {
        Map<String, Object> claimsMap = new HashMap<>();
        
        // Extract standard PASETO claims
        if (claims.getSubject() != null) {
            claimsMap.put("sub", claims.getSubject());
        }
        if (claims.getAudience() != null) {
            claimsMap.put("aud", claims.getAudience());
        }
        if (claims.getIssuer() != null) {
            claimsMap.put("iss", claims.getIssuer());
        }
        if (claims.getIssuedAt() != null) {
            claimsMap.put("iat", claims.getIssuedAt());
        }
        if (claims.getExpiration() != null) {
            claimsMap.put("exp", claims.getExpiration());
        }
        if (claims.getNotBefore() != null) {
            claimsMap.put("nbf", claims.getNotBefore());
        }
        if (claims.getTokenId() != null) {
            claimsMap.put("jti", claims.getTokenId());
        }

        // Extract custom claims
        claims.forEach(claimsMap::put);
        
        return claimsMap;
    }
}
package com.juu.juulabel.common.provider.jwt;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.juu.juulabel.auth.domain.SignUpToken;
import com.juu.juulabel.auth.service.SocialLinkService;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class SignupTokenProvider extends JwtTokenProvider {

    private final SocialLinkService socialLinkService;

    public SignupTokenProvider(@Value("${spring.jwt.signup-key}") String secretKey,
            SocialLinkService socialLinkService) {
        super(secretKey);
        this.socialLinkService = socialLinkService;
    }

    public String createToken(OAuthUser oAuthUser, String nonce) {
        String email = oAuthUser.email();
        Provider provider = oAuthUser.provider();
        String providerId = oAuthUser.id();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("provider", provider.name());
        claims.put("providerId", providerId);
        claims.put("nonce", nonce);
        claims.put("aud", "user-signup-completion");
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .issuer(ISSUER)
                .expiration(new Date(System.currentTimeMillis() + AuthConstants.SIGN_UP_TOKEN_DURATION.toMillis()))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        return extractFromClaims(token, claims -> {
            SignUpToken signUpToken = buildSignUpToken(token);
            socialLinkService.verify(signUpToken);

            return new UsernamePasswordAuthenticationToken(signUpToken, null,
                    Collections.emptyList());
        });
    }

    public SignUpToken buildSignUpToken(String token) {
        Claims claims = parseClaims(token);
        String email = getClaimAsString(claims, "email");
        Provider provider = Provider.valueOf(getClaimAsString(claims, "provider"));
        String providerId = getClaimAsString(claims, "providerId");
        String nonce = getClaimAsString(claims, "nonce");
        String aud = getClaimAsString(claims, "aud");

        if (!"[user-signup-completion]".equals(aud)) {
            throw new AuthException(ErrorCode.INVALID_AUTHENTICATION);
        }

        return new SignUpToken(token, email, provider, providerId, nonce);
    }

    /**
     * Safely extract claim as string, handling potential collection types
     */
    private String getClaimAsString(Claims claims, String claimName) {
        Object claimValue = claims.get(claimName);
        if (claimValue == null) {
            return null;
        }
        return claimValue.toString();
    }

}

package com.juu.juulabel.common.provider.token.validator;

import java.util.Map;

import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Provider;

/**
 * Record to hold signup token claims
 */
public record SignupTokenClaims(
        String email,
        Provider provider,
        String providerId,
        String nonce,
        String audience) {
    public static SignupTokenClaims from(Map<String, Object> claims) {
        return new SignupTokenClaims(
                getRequiredClaim(claims, "email"),
                Provider.valueOf(getRequiredClaim(claims, "provider")),
                getRequiredClaim(claims, "providerId"),
                getRequiredClaim(claims, "nonce"),
                getRequiredClaim(claims, "aud"));
    }

    private static String getRequiredClaim(Map<String, Object> claims, String claimName) {
        Object value = claims.get(claimName);
        if (value == null) {
            throw new AuthException("Missing required claim: " + claimName, ErrorCode.INVALID_AUTHENTICATION);
        }
        return value.toString();
    }
}
package com.juu.juulabel.common.provider.token;

import java.util.Map;
import java.util.function.Function;

/**
 * Generic token service interface that separates token operations
 * from specific validation and business logic.
 */
public interface TokenService<T> {

    /**
     * Creates a token with the provided claims
     */
    String createToken(Map<String, Object> claimsMap);

    /**
     * Parses and validates a token, returning the claims
     */
    T parseToken(String token);

    /**
     * Extracts specific information from token claims
     */
    <R> R extractFromToken(String token, Function<T, R> extractor);

    /**
     * Validates if a token is structurally valid
     */
    boolean isValidToken(String token);
}
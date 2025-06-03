package com.juu.juulabel.common.session;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for generating cryptographically secure session tokens
 */
@Slf4j
@Component
public class SessionTokenGenerator {

    private static final int TOKEN_LENGTH = 32;
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a unique session token with collision detection
     * @param existenceChecker Function to check if a token already exists
     * @return Unique session token
     */
    public String generateUniqueToken(Predicate<String> existenceChecker) {
        for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
            String token = generateSecureToken();

            if (!existenceChecker.test(token)) {
                return token;
            }

            log.warn("Session token collision detected, retrying... Attempt: {}", attempt + 1);
        }

        throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * Generates a simple secure token without collision detection
     */
    public String generateSecureToken() {
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
} 
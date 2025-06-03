package com.juu.juulabel.common.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.http.CookieService;
import com.juu.juulabel.redis.UserSessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication strategy for user session validation.
 * Handles regular authenticated requests using session cookies.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserSessionAuthenticationStrategy implements AuthenticationStrategy {

    private final UserSessionManager sessionManager;
    private final CookieService cookieService;

    @Override
    public boolean canHandle(HttpServletRequest request) {
        // This strategy handles any request with a session token
        // (but lower priority than signup token strategy)
        return cookieService.getCookie(AuthConstants.AUTH_TOKEN_NAME).isPresent();
    }

    @Override
    public Authentication authenticate(HttpServletRequest request) {
        String authToken = cookieService.getCookie(AuthConstants.AUTH_TOKEN_NAME)
                .orElse(null);

        if (authToken == null || authToken.trim().isEmpty()) {
            log.debug("No auth token found for request: {}", request.getRequestURI());
            return null; // Not an error - just no authentication
        }

        try {
            Authentication authentication = sessionManager.getAuthentication(authToken);
            log.debug("Session authentication successful for: {}", 
                     authentication.getName());
            return authentication;
            
        } catch (Exception e) {
            log.warn("Session authentication failed for token: {} - {}", 
                    maskToken(authToken), e.getMessage());
            return null; // Don't throw exception - let request proceed unauthenticated
        }
    }

    @Override
    public int getOrder() {
        return 50; // Lower priority than signup token
    }

    @Override
    public String getStrategyName() {
        return "UserSession";
    }

    /**
     * Masks sensitive token for logging
     */
    private String maskToken(String token) {
        if (token == null || token.length() < 8) {
            return "***";
        }
        return token.substring(0, 4) + "***" + token.substring(token.length() - 4);
    }
} 
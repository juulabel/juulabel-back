package com.juu.juulabel.common.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.juu.juulabel.auth.service.SignupTokenService;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.http.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication strategy for signup token validation.
 * Handles requests to signup endpoints that require signup token.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SignupTokenAuthenticationStrategy implements AuthenticationStrategy {

    private static final String SIGNUP_PATH_PREFIX = "/v1/api/auth/sign-up";

    private final SignupTokenService signupTokenService;
    private final CookieService cookieService;

    @Override
    public boolean canHandle(HttpServletRequest request) {
        return request.getRequestURI().startsWith(SIGNUP_PATH_PREFIX);
    }

    @Override
    public Authentication authenticate(HttpServletRequest request) {
        String signupToken = cookieService.getCookie(AuthConstants.SIGN_UP_TOKEN_NAME)
                .orElse(null);

        if (signupToken == null || signupToken.trim().isEmpty()) {
            log.warn("Signup token missing for signup request: {}", request.getRequestURI());
            throw new AuthException(ErrorCode.SIGN_UP_TOKEN_NOT_FOUND);
        }

        try {
            String token = signupTokenService.resolveToken(signupToken);
            Authentication authentication = signupTokenService.getAuthentication(token);

            log.debug("Signup token authentication successful for: {}",
                    authentication.getName());
            return authentication;

        } catch (Exception e) {
            log.warn("Signup token validation failed: {}", e.getMessage());
            throw new AuthException(ErrorCode.SIGN_UP_SESSION_EXPIRED);
        }
    }

    @Override
    public int getOrder() {
        return 10; // High priority for signup requests
    }

    @Override
    public String getStrategyName() {
        return "SignupToken";
    }
}
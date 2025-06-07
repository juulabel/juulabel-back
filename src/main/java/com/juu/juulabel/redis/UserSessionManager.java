package com.juu.juulabel.redis;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.http.CookieService;
import com.juu.juulabel.common.http.IpAddressService;
import com.juu.juulabel.common.http.RequestDataExtractor;
import com.juu.juulabel.common.session.SessionAuthenticationProvider;
import com.juu.juulabel.common.session.SessionService;
import com.juu.juulabel.common.session.SessionTokenGenerator;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.token.UserSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Orchestrates user session workflows using separated services
 * Replaces the old SessionManager with better separation of concerns
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionManager {

    private final SessionService<UserSession, String> sessionService;
    private final SessionTokenGenerator tokenGenerator;
    private final SessionAuthenticationProvider authenticationProvider;
    private final CookieService cookieService;
    private final RequestDataExtractor requestDataExtractor;
    private final IpAddressService ipAddressService;

    /**
     * Creates authentication from current session token
     */
    public Authentication getAuthentication(String authToken) {
        UserSession session = getValidatedSession(authToken);
        return authenticationProvider.createAuthentication(session);
    }

    /**
     * Creates new session for member
     */
    public void createSession(Member member) {
        // Generate unique session ID
        String sessionId = tokenGenerator.generateUniqueToken(sessionService::sessionExists);

        // Create session with current request context
        UserSession session = UserSession.createFromMember(
                sessionId,
                member,
                requestDataExtractor.getDeviceId(),
                ipAddressService.getClientIpAddress(),
                requestDataExtractor.getUserAgent().orElse("unknown"));

        // Save session and set cookie
        sessionService.createSession(session);
        cookieService.addCookie(AuthConstants.AUTH_TOKEN_NAME, sessionId, AuthConstants.USER_SESSION_TTL);

        cookieService.removeCookie(AuthConstants.SIGN_UP_TOKEN_NAME);

        log.debug("Session created successfully for member: {}", member.getEmail());
    }

    /**
     * Retrieves and validates current session, updating activity
     */
    public UserSession getValidatedSession(String authToken) {
        UserSession session = sessionService.getSession(authToken)
                .orElseThrow(() -> {
                    log.warn("Session not found for token: {}", maskToken(authToken));
                    return new AuthException(ErrorCode.USER_SESSION_EXPIRED);
                });

        // Update session activity (immutable approach)
        UserSession updatedSession = session.withUpdatedLastAccessed();
        sessionService.updateSession(updatedSession);

        return updatedSession;
    }

    /**
     * Invalidates current user session
     */
    public void invalidateSession() {
        String authToken = cookieService.getCookie(AuthConstants.AUTH_TOKEN_NAME)
                .orElse(null);

        if (authToken != null) {
            sessionService.deleteSession(authToken);
        }
        cookieService.removeCookie(AuthConstants.AUTH_TOKEN_NAME);
    }

    /**
     * Invalidates all sessions for a user
     */
    public void invalidateAllUserSessions(Long userId) {
        sessionService.deleteAllUserSessions(userId);
        cookieService.removeCookie(AuthConstants.AUTH_TOKEN_NAME);
        log.debug("All sessions invalidated for user: {}", userId);
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
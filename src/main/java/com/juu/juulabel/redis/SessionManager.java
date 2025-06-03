package com.juu.juulabel.redis;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.juu.juulabel.auth.repository.UserSessionRepository;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.util.CookieUtil;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.token.UserSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionManager {

    private static final int TOKEN_LENGTH = 32;
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final SecureRandom secureRandom = new SecureRandom();
    private final UserSessionRepository userSessionRepository;
    private final CookieUtil cookieUtil;

    /**
     * Creates authentication from current session
     */
    public Authentication getAuthentication(String authToken) {

        UserSession session = getSession(authToken);

        Member member = Member.builder()
                .id(session.getMemberId())
                .role(session.getRole())
                .email(session.getEmail())
                .build();

        return new UsernamePasswordAuthenticationToken(
                member,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(session.getRole().name())));
    }

    /**
     * Creates new session for member with collision detection
     */
    public void createSession(Member member) {

        String sessionId = generateUniqueSessionId();
        UserSession session = new UserSession(sessionId, member);

        try {
            userSessionRepository.save(session);
            cookieUtil.addCookie(AuthConstants.AUTH_TOKEN_NAME, sessionId, AuthConstants.USER_SESSION_TTL);

            log.debug("Session created successfully for member: {}", member.getEmail());
        } catch (Exception e) {
            log.error("Failed to create session for member: {}", member.getEmail(), e);
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves and validates current session
     */
    public UserSession getSession(String authToken) {

        Optional<UserSession> sessionOpt = userSessionRepository.findById(authToken);
        if (sessionOpt.isEmpty()) {
            log.warn("Session not found for token: {}", maskToken(authToken));
            throw new AuthException(ErrorCode.USER_SESSION_EXPIRED);
        }

        UserSession session = sessionOpt.get();
        updateSessionActivity(session);

        return session;
    }

    /**
     * Invalidates current user session
     */
    public void invalidateSession() {
        String authToken = cookieUtil.getCookie(AuthConstants.AUTH_TOKEN_NAME);

        userSessionRepository.deleteById(authToken);
        cookieUtil.removeCookie(AuthConstants.AUTH_TOKEN_NAME);
    }

    /**
     * Invalidates all sessions for a user
     */
    public void invalidateAllUserSessions(Long userId) {

        try {
            userSessionRepository.deleteAllByMemberId(userId);
            cookieUtil.removeCookie(AuthConstants.AUTH_TOKEN_NAME);

            log.debug("All sessions invalidated for user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to invalidate all sessions for user: {}", userId, e);
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // Private helper methods

    /**
     * Generates unique session ID with collision detection
     */
    private String generateUniqueSessionId() {
        for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
            String sessionId = generateSecureToken();

            if (!userSessionRepository.existsById(sessionId)) {
                return sessionId;
            }

            log.warn("Session ID collision detected, retrying... Attempt: {}", attempt + 1);
        }

        throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * Generates cryptographically secure random token
     */
    private String generateSecureToken() {
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /**
     * Updates session activity timestamp
     */
    private void updateSessionActivity(UserSession session) {
        try {
            session.updateLastAccessed();
            userSessionRepository.save(session);
        } catch (Exception e) {
            log.warn("Failed to update session activity for session: {}", session.getId(), e);
            // Non-critical operation, don't throw exception
        }
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

package com.juu.juulabel.redis;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.juu.juulabel.auth.repository.UserSessionRepository;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.session.SessionService;
import com.juu.juulabel.member.token.UserSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis-specific implementation of SessionService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSessionService implements SessionService<UserSession, String> {

    private final UserSessionRepository userSessionRepository;

    @Override
    public UserSession createSession(UserSession session) {
        try {
            return userSessionRepository.save(session);
        } catch (Exception e) {
            log.error("Failed to create session for member: {}", session.getEmail(), e);
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<UserSession> getSession(String sessionId) {
        return userSessionRepository.findById(sessionId);
    }

    @Override
    public UserSession updateSession(UserSession session) {
        try {
            return userSessionRepository.save(session);
        } catch (Exception e) {
            log.warn("Failed to update session: {}", session.getId(), e);
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteSession(String sessionId) {
        userSessionRepository.deleteById(sessionId);
    }

    @Override
    public void deleteAllUserSessions(Long userId) {
        try {
            userSessionRepository.deleteAllByMemberId(userId);
            log.debug("All sessions deleted for user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to delete all sessions for user: {}", userId, e);
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean sessionExists(String sessionId) {
        return userSessionRepository.existsById(sessionId);
    }
} 
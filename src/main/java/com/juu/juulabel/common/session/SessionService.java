package com.juu.juulabel.common.session;

import java.util.Optional;

/**
 * Generic session management interface - technology agnostic
 * 
 * @param <T>  Session entity type
 * @param <ID> Session identifier type
 */
public interface SessionService<T, ID> {

    /**
     * Creates a new session
     */
    T createSession(T session);

    /**
     * Retrieves a session by ID
     */
    Optional<T> getSession(ID sessionId);

    /**
     * Updates an existing session
     */
    T updateSession(T session);

    /**
     * Deletes a session by ID
     */
    void deleteSession(ID sessionId);

    /**
     * Deletes all sessions for a specific user
     */
    void deleteAllUserSessions(Long userId);

    /**
     * Checks if a session exists
     */
    boolean sessionExists(ID sessionId);
}
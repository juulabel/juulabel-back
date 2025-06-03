package com.juu.juulabel.common.auth;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Strategy interface for different authentication types.
 * Allows the authorization filter to handle different auth flows cleanly.
 */
public interface AuthenticationStrategy {

    /**
     * Checks if this strategy can handle the current request
     */
    boolean canHandle(HttpServletRequest request);

    /**
     * Creates authentication from the request
     * @param request HTTP request containing authentication data
     * @return Authentication object or null if not authenticated
     */
    Authentication authenticate(HttpServletRequest request);

    /**
     * Returns the priority order of this strategy (lower = higher priority)
     */
    default int getOrder() {
        return 100;
    }

    /**
     * Returns the name of this authentication strategy for logging
     */
    String getStrategyName();
} 
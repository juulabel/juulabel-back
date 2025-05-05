package com.juu.juulabel.common.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for HTTP request operations
 */
@Component
public class HttpRequestUtil {

    /**
     * Extract client IP address from request
     * Handles X-Forwarded-For header for clients behind a proxy
     */
    public String extractIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        return xForwardedFor != null && !xForwardedFor.isEmpty()
                ? xForwardedFor.split(",")[0].trim()
                : request.getRemoteAddr();
    }

    public String extractUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    public String extractAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);

    }
}

package com.juu.juulabel.common.util;

import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for authorization header extraction
 */
public final class AuthorizationExtractor extends AbstractHttpUtil {

    /**
     * Private constructor to prevent instantiation
     */
    private AuthorizationExtractor() {
        super();
    }

    /**
     * Extract authorization header from request
     * 
     * @return authorization header value
     */
    public static String getAuthorization() {
        HttpServletRequest request = getCurrentRequest();
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
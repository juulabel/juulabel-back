package com.juu.juulabel.common.util;

import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for user agent extraction
 */
public final class UserAgentExtractor extends AbstractHttpUtil {

    /**
     * Private constructor to prevent instantiation
     */
    private UserAgentExtractor() {
        super();
    }

    /**
     * Extract user agent from request headers
     * 
     * @return user agent string from User-Agent header
     */
    public static String getUserAgent() {
        HttpServletRequest request = getCurrentRequest();
        return request.getHeader(HttpHeaders.USER_AGENT);
    }
}
package com.juu.juulabel.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utility class for cookie management operations
 */

public final class CookieUtil extends AbstractHttpUtil {

    private CookieUtil() {
        // Private constructor to prevent instantiation
    }

    public static String getCookie(String name) {
        HttpServletRequest request = getCurrentRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Adds a secure HTTP-only cookie to the response
     * 
     * @param name   the cookie name
     * @param value  the cookie value
     * @param maxAge the cookie max age in seconds
     */
    public static void addCookie(String name, String value, int maxAge) {
        HttpServletResponse response = getCurrentResponse();
        Cookie cookie = createSecureCookie(name, value, maxAge);
        response.addCookie(cookie);
    }

    /**
     * Removes a cookie by setting its max age to 0
     * 
     * @param name the cookie name to remove
     */
    public static void removeCookie(String name) {
        addCookie(name, "", 0);
    }

    /**
     * Creates a secure cookie with default security settings
     * 
     * @param name   the cookie name
     * @param value  the cookie value
     * @param maxAge the cookie max age in seconds
     * @return a configured secure cookie
     */
    private static Cookie createSecureCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // TODO: 추후 환경에 따라 설정 변경 필요
        // cookie.setSecure(true);
        return cookie;
    }
}
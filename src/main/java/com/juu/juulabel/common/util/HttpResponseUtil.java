package com.juu.juulabel.common.util;

import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public final class HttpResponseUtil extends AbstractHttpUtil {

    private HttpResponseUtil() {
        super();
    }

    /**
     * Adds a secure HTTP-only cookie to the response
     */
    public static void addCookie(String name, String value, int maxAge) {
        HttpServletResponse response = getCurrentResponse();
        Cookie cookie = createSecureCookie(name, value, maxAge);
        response.addCookie(cookie);
    }

    /**
     * Removes a cookie by setting its max age to 0
     */
    public static void removeCookie(String name) {
        addCookie(name, "", 0);
    }

    /**
     * Creates a secure cookie with default settings
     */
    private static Cookie createSecureCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    public static HttpServletResponse getCurrentResponse() {
        return getFromRequestAttributes(ServletRequestAttributes::getResponse);
    }

}

package com.juu.juulabel.common.util;

import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public final class HttpResponseUtil extends AbstractHttpUtil {

    private HttpResponseUtil() {
        super();
    }

    public static void addCookie(String name, String value, int maxAge) {
        HttpServletResponse response = getCurrentResponse();
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
    }

    public static HttpServletResponse getCurrentResponse() {
        return getFromRequestAttributes(ServletRequestAttributes::getResponse);
    }

}

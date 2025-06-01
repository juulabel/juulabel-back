package com.juu.juulabel.common.util;

import com.juu.juulabel.common.properties.CookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * Utility class for secure cookie management operations.
 * Provides methods for creating, retrieving, and removing HTTP cookies with
 * security best practices.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class CookieUtil extends AbstractHttpUtil {

    // Cookie removal configuration
    private static final int COOKIE_REMOVAL_MAX_AGE = 0;
    private static final String EMPTY_VALUE = "";

    private final CookieProperties cookieProperties;

    /**
     * Retrieves a cookie value by name from the current HTTP request.
     * 
     * @param name the cookie name to search for
     * @return the cookie value if found, null otherwise
     */
    public String getCookie(String name) {
        HttpServletRequest request = getCurrentRequest();
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            log.debug("No cookies found in request");
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a cookie as an Optional to avoid null pointer exceptions.
     * 
     * @param name the cookie name to search for
     * @return Optional containing the cookie value if found, empty otherwise
     */
    public Optional<String> getCookieOptional(String name) {
        return Optional.ofNullable(getCookie(name));
    }

    /**
     * Adds a secure HTTP-only cookie to the response with comprehensive security
     * settings.
     * 
     * @param name   the cookie name
     * @param value  the cookie value
     * @param maxAge the cookie max age in seconds
     */
    public void addCookie(String name, String value, int maxAge) {
        HttpServletResponse response = getCurrentResponse();
        Cookie cookie = createSecureCookie(name, value, maxAge);
        response.addCookie(cookie);
    }

    /**
     * Adds a cookie with default security settings from configuration.
     * 
     * @param name   the cookie name
     * @param value  the cookie value
     * @param maxAge the cookie max age in seconds
     */
    public void addSecureCookie(String name, String value, int maxAge) {
        addCookie(name, value, maxAge);
    }

    /**
     * Removes a cookie by setting its max age to 0 and clearing its value.
     * This method ensures proper cookie removal across different browsers.
     * 
     * @param name the cookie name to remove
     */
    public void removeCookie(String name) {
        HttpServletResponse response = getCurrentResponse();

        // Create removal cookie with both secure and non-secure variants
        // to ensure removal regardless of original cookie settings
        Cookie removeCookie = createRemovalCookie(name, false);
        response.addCookie(removeCookie);

        // Also add secure variant for removal
        Cookie secureRemoveCookie = createRemovalCookie(name, true);
        response.addCookie(secureRemoveCookie);
    }

    /**
     * Checks if a cookie with the given name exists in the current request.
     * 
     * @param name the cookie name to check
     * @return true if cookie exists, false otherwise
     */
    public boolean cookieExists(String name) {
        return getCookie(name) != null;
    }

    /**
     * Creates a secure cookie with comprehensive security settings.
     */
    private Cookie createSecureCookie(String name, String value, int maxAge) {
        boolean isSecure = cookieProperties.isSecure();
        Cookie cookie = new Cookie(name, value);

        // Set domain only for production/secure environments
        if (isSecure) {
            cookie.setDomain(cookieProperties.getDomain());
        }

        cookie.setPath(cookieProperties.getPath());
        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setSecure(isSecure);
        cookie.setMaxAge(maxAge);

        // Set SameSite attribute based on security requirements
        String sameSite = isSecure ? cookieProperties.getSameSiteSecure() : cookieProperties.getSameSiteNonSecure();
        cookie.setAttribute("SameSite", sameSite);

        return cookie;
    }

    /**
     * Creates a cookie specifically for removal purposes.
     */
    private Cookie createRemovalCookie(String name, boolean isSecure) {
        Cookie cookie = new Cookie(name, EMPTY_VALUE);

        if (isSecure) {
            cookie.setDomain(cookieProperties.getDomain());
            cookie.setSecure(true);
        }

        cookie.setPath(cookieProperties.getPath());
        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setMaxAge(COOKIE_REMOVAL_MAX_AGE);

        return cookie;
    }
}
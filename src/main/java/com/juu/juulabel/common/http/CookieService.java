package com.juu.juulabel.common.http;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.juu.juulabel.common.properties.CookieProperties;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for secure cookie management operations.
 * Provides methods for creating, retrieving, and removing HTTP cookies with
 * security best practices.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CookieService {

    // Cookie removal configuration
    private static final int COOKIE_REMOVAL_MAX_AGE = 0;
    private static final String EMPTY_VALUE = "";

    private final HttpContextService httpContextService;
    private final CookieProperties cookieProperties;

    /**
     * Retrieves a cookie value by name from the current HTTP request
     * 
     * @param name the cookie name to search for
     * @return the cookie value if found, null otherwise
     */
    public Optional<String> getCookie(String name) {
        return httpContextService.getCurrentRequestOptional()
                .map(HttpServletRequest::getCookies)
                .map(cookies -> findCookieByName(cookies, name))
                .orElse(Optional.empty());
    }

    /**
     * Adds a secure HTTP-only cookie to the response with comprehensive security
     * settings
     * 
     * @param name   the cookie name
     * @param value  the cookie value
     * @param maxAge the cookie max age in seconds
     */
    public void addCookie(String name, String value, int maxAge) {
        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    Cookie cookie = createCookie(name, value, maxAge);
                    response.addCookie(cookie);
                    log.debug("Added secure cookie: {} with maxAge: {}", name, maxAge);
                },
                () -> log.warn("Cannot add cookie '{}' - no HTTP response context available", name));
    }

    /**
     * Removes a cookie by setting its max age to 0 and clearing its value.
     * This method ensures proper cookie removal across different browsers.
     * 
     * @param name the cookie name to remove
     */
    public void removeCookie(String name) {
        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    // Create removal cookie with both secure and non-secure variants
                    // to ensure removal regardless of original cookie settings
                    Cookie removeCookie = createRemovalCookie(name);
                    response.addCookie(removeCookie);

                    log.debug("Removed cookie: {}", name);
                },
                () -> log.warn("Cannot remove cookie '{}' - no HTTP response context available", name));
    }

    /**
     * Checks if a cookie with the given name exists in the current request
     * 
     * @param name the cookie name to check
     * @return true if cookie exists, false otherwise
     */
    public boolean cookieExists(String name) {
        return getCookie(name).isPresent();
    }

    /**
     * Gets all cookies from the current request
     * 
     * @return array of cookies, or empty array if none exist
     */
    public Cookie[] getAllCookies() {
        return httpContextService.getCurrentRequestOptional()
                .map(HttpServletRequest::getCookies)
                .orElse(new Cookie[0]);
    }

    /**
     * Validates cookie name according to RFC standards
     * 
     * @param name cookie name to validate
     * @return true if valid cookie name
     */
    public boolean isValidCookieName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // Basic validation - no spaces, control characters, or special chars
        return name.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * Creates a secure cookie with comprehensive security settings
     */
    private Cookie createCookie(String name, String value, int maxAge) {
        boolean isSecure = cookieProperties.isSecure();
        Cookie cookie = new Cookie(name, value);
        
        cookie.setPath(cookieProperties.getPath());
        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setSecure(isSecure);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", cookieProperties.getSameSite());

        return cookie;
    }

    /**
     * Creates a cookie specifically for removal purposes
     */
    private Cookie createRemovalCookie(String name) {
        boolean isSecure = cookieProperties.isSecure();
        Cookie cookie = new Cookie(name, EMPTY_VALUE);
        
        cookie.setPath(cookieProperties.getPath());
        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setSecure(isSecure);
        cookie.setMaxAge(COOKIE_REMOVAL_MAX_AGE);
        cookie.setAttribute("SameSite", cookieProperties.getSameSite());

        return cookie;
    }

    /**
     * Helper method to find cookie by name in cookie array
     */
    private Optional<String> findCookieByName(Cookie[] cookies, String name) {
        if (cookies == null) {
            log.debug("No cookies found in request");
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
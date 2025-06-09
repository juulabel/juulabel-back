package com.juu.juulabel.common.http;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.properties.RedirectProperties;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for HTTP response operations.
 * Handles redirects, status codes, headers, and response manipulation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HttpResponseService {

    private final HttpContextService httpContextService;
    private final RedirectProperties redirectProperties;

    /**
     * Redirects to the configured login URL
     */
    public void redirectToLogin() {
        redirect(redirectProperties.getLoginUrl());
        log.debug("Redirected to login page");
    }

    /**
     * Redirects to the configured signup URL
     */
    public void redirectToSignup(String email) {
        redirect(redirectProperties.getSignupUrl(email));
        log.debug("Redirected to signup page");
    }

    /**
     * Redirects to the configured error URL
     */
    public void redirectToError() {
        redirect(redirectProperties.getErrorUrl());
        log.debug("Redirected to error page");
    }

    /**
     * Performs redirect to specified URL
     * @param url Target URL for redirect
     * @throws BaseException if redirect fails or no response context available
     */
    public void redirect(String url) {
        if (url == null || url.trim().isEmpty()) {
            log.error("Redirect URL is null or empty");
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    try {
                        response.sendRedirect(url);
                        log.debug("Successfully redirected to: {}", url);
                    } catch (IOException e) {
                        log.error("Failed to redirect to URL: {} - {}", url, e.getMessage());
                        throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                },
                () -> {
                    log.error("Cannot redirect to '{}' - no HTTP response context available", url);
                    throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
        );
    }

    /**
     * Sets response status code
     * @param status HTTP status to set
     */
    public void setStatus(HttpStatus status) {
        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    response.setStatus(status.value());
                    log.debug("Set response status to: {}", status);
                },
                () -> log.warn("Cannot set status '{}' - no HTTP response context available", status)
        );
    }

    /**
     * Adds header to response
     * @param name Header name
     * @param value Header value
     */
    public void addHeader(String name, String value) {
        if (name == null || name.trim().isEmpty()) {
            log.warn("Header name is null or empty");
            return;
        }

        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    response.addHeader(name, value);
                    log.debug("Added header: {} = {}", name, value);
                },
                () -> log.warn("Cannot add header '{}' - no HTTP response context available", name)
        );
    }

    /**
     * Sets content type for response
     * @param contentType Content type to set
     */
    public void setContentType(String contentType) {
        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    response.setContentType(contentType);
                    log.debug("Set content type to: {}", contentType);
                },
                () -> log.warn("Cannot set content type '{}' - no HTTP response context available", contentType)
        );
    }

    /**
     * Sets content type using MediaType enum
     * @param mediaType MediaType to set
     */
    public void setContentType(MediaType mediaType) {
        setContentType(mediaType.toString());
    }

    /**
     * Sets character encoding for response
     * @param encoding Character encoding to set
     */
    public void setCharacterEncoding(String encoding) {
        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    response.setCharacterEncoding(encoding);
                    log.debug("Set character encoding to: {}", encoding);
                },
                () -> log.warn("Cannot set character encoding '{}' - no HTTP response context available", encoding)
        );
    }

    /**
     * Writes content to response
     * @param content Content to write
     * @throws BaseException if writing fails
     */
    public void writeContent(String content) {
        httpContextService.getCurrentResponseOptional().ifPresentOrElse(
                response -> {
                    try {
                        response.getWriter().write(content);
                        log.debug("Successfully wrote content to response (length: {})", content.length());
                    } catch (IOException e) {
                        log.error("Failed to write content to response: {}", e.getMessage());
                        throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                },
                () -> {
                    log.error("Cannot write content - no HTTP response context available");
                    throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
        );
    }

    /**
     * Sets cache control headers to prevent caching
     */
    public void setNoCache() {
        addHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        addHeader(HttpHeaders.PRAGMA, "no-cache");
        addHeader(HttpHeaders.EXPIRES, "0");
        log.debug("Set no-cache headers");
    }

    /**
     * Sets cache control headers for specified max age
     * @param maxAgeSeconds Maximum age in seconds
     */
    public void setCacheMaxAge(int maxAgeSeconds) {
        addHeader(HttpHeaders.CACHE_CONTROL, "max-age=" + maxAgeSeconds);
        log.debug("Set cache max-age to: {} seconds", maxAgeSeconds);
    }

    /**
     * Checks if response is committed (headers already sent)
     * @return true if response is committed
     */
    public boolean isCommitted() {
        return httpContextService.getCurrentResponseOptional()
                .map(HttpServletResponse::isCommitted)
                .orElse(true); // Assume committed if no context
    }

    /**
     * Gets current response status if available
     * @return Optional containing status code
     */
    public Optional<Integer> getStatus() {
        return httpContextService.getCurrentResponseOptional()
                .map(HttpServletResponse::getStatus);
    }

    /**
     * Safely redirects with fallback error handling
     * @param url Target URL
     * @param fallbackUrl Fallback URL if primary fails
     */
    public void safeRedirect(String url, String fallbackUrl) {
        try {
            redirect(url);
        } catch (Exception e) {
            log.warn("Primary redirect to '{}' failed, trying fallback: {}", url, e.getMessage());
            try {
                redirect(fallbackUrl);
            } catch (Exception fallbackException) {
                log.error("Both primary and fallback redirects failed: {}", fallbackException.getMessage());
                throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }
} 
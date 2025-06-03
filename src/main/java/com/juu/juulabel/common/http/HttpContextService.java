package com.juu.juulabel.common.http;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for accessing HTTP context (request/response) from the current thread.
 * Provides safe access to servlet objects with proper error handling.
 */
@Slf4j
@Service
public class HttpContextService {

    /**
     * Gets the current HTTP request from the servlet context
     * @return the current HttpServletRequest
     * @throws BaseException if request context is not available
     */
    public HttpServletRequest getCurrentRequest() {
        return getServletRequestAttributes()
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> {
                    log.error("No HTTP request context available");
                    return new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
                });
    }

    /**
     * Gets the current HTTP response from the servlet context
     * @return the current HttpServletResponse
     * @throws BaseException if request context is not available
     */
    public HttpServletResponse getCurrentResponse() {
        return getServletRequestAttributes()
                .map(ServletRequestAttributes::getResponse)
                .orElseThrow(() -> {
                    log.error("No HTTP response context available");
                    return new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
                });
    }

    /**
     * Safely gets the current request if available
     * @return Optional containing the request, or empty if not available
     */
    public Optional<HttpServletRequest> getCurrentRequestOptional() {
        try {
            return Optional.of(getCurrentRequest());
        } catch (BaseException e) {
            log.debug("HTTP request context not available: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Safely gets the current response if available
     * @return Optional containing the response, or empty if not available
     */
    public Optional<HttpServletResponse> getCurrentResponseOptional() {
        try {
            return Optional.of(getCurrentResponse());
        } catch (BaseException e) {
            log.debug("HTTP response context not available: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Checks if HTTP context is currently available
     * @return true if context is available, false otherwise
     */
    public boolean isContextAvailable() {
        return getServletRequestAttributes().isPresent();
    }

    /**
     * Gets ServletRequestAttributes safely
     */
    private Optional<ServletRequestAttributes> getServletRequestAttributes() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast);
    }
} 
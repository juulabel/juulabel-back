package com.juu.juulabel.common.http;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for extracting various data from HTTP requests.
 * Handles header extraction, path matching, and parameter retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RequestDataExtractor {

    private static final String DEVICE_ID_HEADER_NAME = "Device-Id";
    
    private final HttpContextService httpContextService;

    /**
     * Checks if the current request path matches the given prefix
     * @param pathPrefix Path prefix to match against
     * @return true if path matches, false otherwise
     */
    public boolean isPathMatch(String pathPrefix) {
        return httpContextService.getCurrentRequestOptional()
                .map(HttpServletRequest::getRequestURI)
                .map(uri -> uri.startsWith(pathPrefix))
                .orElse(false);
    }

    /**
     * Extracts Authorization header from current request
     * @return Authorization header value, or null if not present
     */
    public Optional<String> getAuthorizationHeader() {
        return getHeaderValue(HttpHeaders.AUTHORIZATION);
    }

    /**
     * Extracts User-Agent header from current request
     * @return User-Agent header value, or null if not present
     */
    public Optional<String> getUserAgent() {
        return getHeaderValue(HttpHeaders.USER_AGENT);
    }

    /**
     * Extracts device ID from request headers with fallback to parameter
     * @return Device ID value
     * @throws BaseException if Device-Id is missing or empty
     */
    public String getDeviceId() {
        HttpServletRequest request = httpContextService.getCurrentRequest();
        
        // Try header first
        String deviceId = request.getHeader(DEVICE_ID_HEADER_NAME);
        
        // Fallback to state parameter
        if (!StringUtils.hasText(deviceId)) {
            deviceId = request.getParameter("state");
        }

        if (!StringUtils.hasText(deviceId)) {
            log.warn("Device-Id not found in headers or parameters for request: {}", 
                    request.getRequestURI());
            throw new BaseException(ErrorCode.DEVICE_ID_REQUIRED);
        }

        return deviceId.trim();
    }

    /**
     * Safely extracts device ID without throwing exception
     * @return Optional containing device ID if present
     */
    public Optional<String> getDeviceIdOptional() {
        try {
            return Optional.of(getDeviceId());
        } catch (BaseException e) {
            log.debug("Device ID not available: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Gets a specific header value from the current request
     * @param headerName Name of the header to retrieve
     * @return Optional containing header value if present
     */
    public Optional<String> getHeaderValue(String headerName) {
        return httpContextService.getCurrentRequestOptional()
                .map(request -> request.getHeader(headerName))
                .filter(StringUtils::hasText);
    }

    /**
     * Gets a specific parameter value from the current request
     * @param parameterName Name of the parameter to retrieve
     * @return Optional containing parameter value if present
     */
    public Optional<String> getParameterValue(String parameterName) {
        return httpContextService.getCurrentRequestOptional()
                .map(request -> request.getParameter(parameterName))
                .filter(StringUtils::hasText);
    }

    /**
     * Gets the current request URI
     * @return Optional containing request URI if available
     */
    public Optional<String> getRequestURI() {
        return httpContextService.getCurrentRequestOptional()
                .map(HttpServletRequest::getRequestURI);
    }

    /**
     * Gets the current request method
     * @return Optional containing request method if available
     */
    public Optional<String> getRequestMethod() {
        return httpContextService.getCurrentRequestOptional()
                .map(HttpServletRequest::getMethod);
    }

    /**
     * Gets the remote address from the request
     * @return Optional containing remote address if available
     */
    public Optional<String> getRemoteAddress() {
        return httpContextService.getCurrentRequestOptional()
                .map(HttpServletRequest::getRemoteAddr);
    }
} 
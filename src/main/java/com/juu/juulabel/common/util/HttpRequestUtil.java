package com.juu.juulabel.common.util;

import org.springframework.http.HttpHeaders;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

public class HttpRequestUtil extends AbstractHttpUtil {

    private static final String DEVICE_ID_HEADER_NAME = "Device-Id";

    private HttpRequestUtil() {
        super();
    }

    public static boolean isPathMatch(String path) {
        return getCurrentRequest().getRequestURI().startsWith(path);
    }

    public static String getAuthorization() {
        HttpServletRequest request = getCurrentRequest();
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * Extract device ID from request headers
     * 
     * @return device ID from Device-Id header
     * @throws BaseException if Device-Id header is missing or empty
     */
    public static String getDeviceId() {
        HttpServletRequest request = getCurrentRequest();
        String deviceId = request.getHeader(DEVICE_ID_HEADER_NAME);
        if (deviceId == null || deviceId.trim().isEmpty()) {
            deviceId = request.getParameter("state");
        }

        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new BaseException(ErrorCode.DEVICE_ID_REQUIRED);
        }
        return deviceId.trim();
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

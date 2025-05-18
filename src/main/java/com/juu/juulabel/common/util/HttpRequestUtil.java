package com.juu.juulabel.common.util;

import org.springframework.http.HttpHeaders;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.juu.juulabel.common.constants.AuthConstants.DEVICE_ID_HEADER_NAME;

/**
 * Utility class for HTTP request operations
 */
public final class HttpRequestUtil extends AbstractHttpUtil {

    private static final String UNKNOWN = "unknown";
    private static final List<String> IP_HEADER_CANDIDATES = List.of(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR");

    /**
     * Private constructor to prevent instantiation of utility class
     */
    private HttpRequestUtil() {
        super();
    }

    public static String getAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public static String getClientIpAddress() {
        HttpServletRequest request = getCurrentRequest();

        return IP_HEADER_CANDIDATES.stream()
                .map(request::getHeader)
                .filter(ip -> ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip))
                .map(ip -> ip.split(",")[0].trim())
                .findFirst()
                .orElseGet(request::getRemoteAddr);
    }

    public static String getUserAgent() {
        return getCurrentRequest().getHeader(HttpHeaders.USER_AGENT);
    }

    public static String getDeviceId() {
        HttpServletRequest request = getCurrentRequest();
        String deviceId = request.getHeader(DEVICE_ID_HEADER_NAME);
        if (deviceId == null) {
            throw new BaseException(ErrorCode.DEVICE_ID_REQUIRED);
        }
        return deviceId;
    }
}

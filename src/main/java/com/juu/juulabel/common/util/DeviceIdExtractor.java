package com.juu.juulabel.common.util;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for device ID extraction
 */
public final class DeviceIdExtractor extends AbstractHttpUtil {

    private static final String DEVICE_ID_HEADER_NAME = "Device-Id";

    /**
     * Private constructor to prevent instantiation
     */
    private DeviceIdExtractor() {
        super();
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
            throw new BaseException(ErrorCode.DEVICE_ID_REQUIRED);
        }
        return deviceId.trim();
    }
} 
package com.juu.juulabel.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.function.Function;

/**
 * Abstract base class for HTTP utility operations
 */
public abstract class AbstractHttpUtil {

    protected AbstractHttpUtil() {
    }

    /**
     * Gets the current HTTP request from the servlet context
     * 
     * @return the current HttpServletRequest
     * @throws BaseException if request attributes are not available
     */
    protected static HttpServletRequest getCurrentRequest() {
        return getFromRequestAttributes(ServletRequestAttributes::getRequest);
    }

    /**
     * Gets the current HTTP response from the servlet context
     * 
     * @return the current HttpServletResponse
     * @throws BaseException if request attributes are not available
     */
    protected static HttpServletResponse getCurrentResponse() {
        return getFromRequestAttributes(ServletRequestAttributes::getResponse);
    }

    /**
     * Extracts data from ServletRequestAttributes using the provided function
     * 
     * @param extractor function to extract data from ServletRequestAttributes
     * @return extracted data
     * @throws BaseException if request attributes are not available
     */
    protected static <T> T getFromRequestAttributes(Function<ServletRequestAttributes, T> extractor) {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(extractor)
                .orElseThrow(() -> new BaseException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
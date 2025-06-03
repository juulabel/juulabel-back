package com.juu.juulabel.common.handler;

import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.util.SecurityResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityResponseUtil securityResponseUtil;

    // Map exception types to error codes for better performance
    private static final Map<Class<? extends Exception>, ErrorCode> CSRF_ERROR_MAP = Map.of(
            InvalidCsrfTokenException.class, ErrorCode.CSRF_TOKEN_INVALID,
            MissingCsrfTokenException.class, ErrorCode.CSRF_TOKEN_MISSING,
            CsrfException.class, ErrorCode.CSRF_TOKEN_MISMATCH);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());
        log.warn("Access denied for request: {} - Exception: {}",
                requestInfo, accessDeniedException.getClass().getSimpleName());

        // Handle CSRF exceptions with optimized lookup
        ErrorCode csrfErrorCode = CSRF_ERROR_MAP.get(accessDeniedException.getClass());
        if (csrfErrorCode != null) {
            handleCsrfException(response, csrfErrorCode, accessDeniedException.getMessage(), requestInfo);
        } else {
            handleAccessDenied(response, accessDeniedException.getMessage(), requestInfo);
        }
    }

    private void handleCsrfException(HttpServletResponse response, ErrorCode errorCode,
            String message, String requestInfo) throws IOException {
        log.warn("CSRF token validation failed for {}: {}", requestInfo, message);
        securityResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN, errorCode, message);
    }

    private void handleAccessDenied(HttpServletResponse response, String message,
            String requestInfo) throws IOException {
        log.warn("Access denied for {}: {}", requestInfo, message);
        securityResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN,
                ErrorCode.INVALID_AUTHENTICATION, message);
    }
}
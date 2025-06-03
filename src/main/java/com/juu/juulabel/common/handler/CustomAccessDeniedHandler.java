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
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityResponseUtil securityResponseUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());
        // Handle CSRF exceptions with optimized lookup

        handleAccessDenied(response, accessDeniedException.getMessage(), requestInfo);
    }

    private void handleAccessDenied(HttpServletResponse response, String message,
            String requestInfo) throws IOException {
        log.warn("Access denied for {}: {}", requestInfo, message);
        securityResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN,
                ErrorCode.INVALID_AUTHENTICATION, message);
    }
}
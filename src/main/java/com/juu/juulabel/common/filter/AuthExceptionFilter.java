package com.juu.juulabel.common.filter;

import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.util.SecurityResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthExceptionFilter extends OncePerRequestFilter {

    private final SecurityResponseUtil securityResponseUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for request {}: {}", request.getRequestURI(), ex.getMessage());
            securityResponseUtil.setErrorResponse(response, HttpStatus.BAD_REQUEST, ex);
        } catch (Exception ex) {
            log.error("Unexpected exception in auth filter for request {}: {}",
                    request.getRequestURI(), ex.getMessage());
            securityResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED,
                    ErrorCode.INVALID_AUTHENTICATION, ex.getMessage());
        }
    }
}
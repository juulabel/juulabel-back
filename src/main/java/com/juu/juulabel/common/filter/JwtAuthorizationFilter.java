package com.juu.juulabel.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.provider.jwt.AccessTokenProvider;
import com.juu.juulabel.common.provider.jwt.SignupTokenProvider;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.common.util.HttpRequestUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AccessTokenProvider accessTokenProvider;
    private final SignupTokenProvider signUpTokenProvider;    
    private final ObjectMapper objectMapper;

    // Cache frequently used paths for better performance
    private static final String SIGNUP_PATH_PREFIX = "/v1/api/auth/sign-up";
    private static final String UTF_8 = "UTF-8";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = extractAuthorizationHeader(request);

            if (authHeader != null) {
                if (isSignUpRequest()) {
                    processSignUpToken(authHeader);
                } else {
                    processAccessToken(authHeader);
                }
            } else if (isSignUpRequest()) {
                // Sign-up requests require authentication
                throw new AuthException(ErrorCode.INVALID_AUTHENTICATION);
            }
            // For other requests without auth header, let Spring Security handle
            // authorization

        } catch (CustomJwtException e) {
            handleJwtException(response, e);
            return;
        } catch (AuthException e) {
            handleAuthException(response, e);
            return;
        } catch (Exception e) {
            log.error("Unexpected error in JWT filter", e);
            handleUnexpectedException(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract Authorization header directly from request for better performance
     */
    private String extractAuthorizationHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * Check if the request is a sign-up request using the available request
     * parameter
     */
    private boolean isSignUpRequest() {
        return HttpRequestUtil.isPathMatch(SIGNUP_PATH_PREFIX);
    }

    /**
     * Process sign-up token with validation
     */
    private void processSignUpToken(String authHeader) {
        try {
            String token = signUpTokenProvider.resolveToken(authHeader);

            Authentication authentication = signUpTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("Unexpected error in sign-up token processing", e);
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Process access token with validation
     */
    private void processAccessToken(String authHeader) {
        String token = accessTokenProvider.resolveToken(authHeader);
        Authentication authentication = accessTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Handle JWT-specific exceptions with appropriate error codes
     */
    private void handleJwtException(HttpServletResponse response, CustomJwtException e) throws IOException {
        writeErrorResponse(response, HttpStatus.UNAUTHORIZED,
                CommonResponse.fail(e.getErrorCode(), e.getMessage()).getBody());
    }

    /**
     * Handle authentication exceptions
     */
    private void handleAuthException(HttpServletResponse response, AuthException e) throws IOException {
        writeErrorResponse(response, HttpStatus.UNAUTHORIZED,
                CommonResponse.fail(e.getErrorCode(), e.getMessage()).getBody());
    }

    /**
     * Handle unexpected exceptions
     */
    private void handleUnexpectedException(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR,
                CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다.").getBody());
    }

    /**
     * Write error response with proper JSON serialization
     */
    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, CommonResponse<?> errorResponse)
            throws IOException {
        response.setCharacterEncoding(UTF_8);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
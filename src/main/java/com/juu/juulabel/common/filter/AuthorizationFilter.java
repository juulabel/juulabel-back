package com.juu.juulabel.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juu.juulabel.common.auth.AuthenticationStrategyResolver;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.response.CommonResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Improved authorization filter using strategy pattern.
 * Delegates authentication logic to specialized strategies.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String UTF_8 = "UTF-8";

    private final AuthenticationStrategyResolver strategyResolver;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Use strategy resolver to determine and apply authentication
            Optional<Authentication> authenticationOpt = strategyResolver.resolveAuthentication(request);

            // Set authentication in security context if present
            authenticationOpt.ifPresent(authentication -> {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.trace("Authentication set in security context for: {}", authentication.getName());
            });

        } catch (AuthException e) {
            log.warn("Authentication failed for request {}: {}", request.getRequestURI(), e.getMessage());
            handleAuthException(response, e);
            return;
        } catch (Exception e) {
            log.error("Unexpected exception during authentication for {}: {}",
                    request.getRequestURI(), e.getMessage());
            handleGenericException(response, e);
            return;
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Handles authentication exceptions with proper error response
     */
    private void handleAuthException(HttpServletResponse response, AuthException e) throws IOException {
        writeErrorResponse(response, HttpStatus.UNAUTHORIZED,
                CommonResponse.fail(e.getErrorCode(), e.getMessage()).getBody());
    }

    /**
     * Handles unexpected exceptions with generic error response
     */
    private void handleGenericException(HttpServletResponse response, Exception e) throws IOException {
        writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR,
                CommonResponse.fail(com.juu.juulabel.common.exception.code.ErrorCode.INTERNAL_SERVER_ERROR,
                        "Authentication error").getBody());
    }

    /**
     * Writes standardized error response
     */
    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, CommonResponse<?> errorResponse)
            throws IOException {
        response.setCharacterEncoding(UTF_8);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
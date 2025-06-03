package com.juu.juulabel.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.util.CookieUtil;
import com.juu.juulabel.common.provider.token.paseto.SignupTokenProvider;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.common.util.HttpRequestUtil;
import com.juu.juulabel.redis.SessionManager;

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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String SIGNUP_PATH_PREFIX = "/v1/api/auth/sign-up";
    private static final String UTF_8 = "UTF-8";
    private static final List<String> ALLOWED_METHODS = List.of("OPTIONS");

    private final SignupTokenProvider signUpTokenProvider;
    private final SessionManager sessionManager;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (isSignUpRequest()) {
                handleSignUpRequest();
            } else {
                if (!ALLOWED_METHODS.contains(request.getMethod())) {
                    handleRegularRequest();
                }
            }
        } catch (AuthException e) {
            handleAuthException(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSignUpRequest() {
        return HttpRequestUtil.isPathMatch(SIGNUP_PATH_PREFIX);
    }

    private void handleSignUpRequest() {
        String signupToken = cookieUtil.getCookie(AuthConstants.SIGN_UP_TOKEN_NAME);

        if (!StringUtils.hasText(signupToken)) {
            throw new AuthException(ErrorCode.SIGN_UP_SESSION_EXPIRED);
        }

        processSignUpToken(signupToken);
    }

    private void handleRegularRequest() {
        String authToken = cookieUtil.getCookie(AuthConstants.AUTH_TOKEN_NAME);

        if (StringUtils.hasText(authToken)) {
            processUserSession(authToken);
        }
    }

    private void processSignUpToken(String signupToken) {
        String token = signUpTokenProvider.resolveToken(signupToken);
        Authentication authentication = signUpTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void processUserSession(String authToken) {
        Authentication authentication = sessionManager.getAuthentication(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleAuthException(HttpServletResponse response, AuthException e) throws IOException {
        writeErrorResponse(response, HttpStatus.UNAUTHORIZED,
                CommonResponse.fail(e.getErrorCode(), e.getMessage()).getBody());
    }

    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, CommonResponse<?> errorResponse)
            throws IOException {
        response.setCharacterEncoding(UTF_8);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
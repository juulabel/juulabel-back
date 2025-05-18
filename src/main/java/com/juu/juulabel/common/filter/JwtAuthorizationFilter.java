package com.juu.juulabel.common.filter;

import com.juu.juulabel.common.provider.JwtTokenProvider;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.juu.juulabel.common.util.HttpRequestUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = HttpRequestUtil.getAuthorization(request);
        if (header != null) {
            String token = jwtTokenProvider.resolveToken(header);
            try {
                if (jwtTokenProvider.isValidateToken(token)) {
                    authenticate(token);
                }
            } catch (ExpiredJwtException | MalformedJwtException e) {
                handleJwtException(response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleJwtException(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(CommonResponse.fail(ErrorCode.INVALID_AUTHENTICATION, "만료되었거나 잘못된 토큰입니다.").toString());
    }

}
package com.juu.juulabel.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.response.CommonResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private static final String UTF_8 = "UTF-8";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            setErrorResponse(response, HttpStatus.BAD_REQUEST,
                    objectMapper.writeValueAsString(CommonResponse.fail(ex)));
        } catch (CustomJwtException ex) {
            setErrorResponse(
                    response,
                    ex.getErrorCode().getHttpStatus(),
                    objectMapper.writeValueAsString(CommonResponse.fail(ex.getErrorCode(), ex.getMessage())));
        }
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String body) throws IOException {
        response.setCharacterEncoding(UTF_8);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
    }
}
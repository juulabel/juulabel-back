package com.juu.juulabel.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.response.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityResponseUtil {

    private final ObjectMapper objectMapper;
    private static final String UTF_8 = "UTF-8";

    /**
     * Sets standardized error response for security-related exceptions
     */
    public void setErrorResponse(HttpServletResponse response, HttpStatus status,
            ErrorCode errorCode, String message) throws IOException {
        String responseBody = objectMapper.writeValueAsString(
                CommonResponse.fail(errorCode, message));
        setResponse(response, status, responseBody);
    }

    /**
     * Sets standardized error response with default error message
     */
    public void setErrorResponse(HttpServletResponse response, HttpStatus status,
            ErrorCode errorCode) throws IOException {
        String responseBody = objectMapper.writeValueAsString(
                CommonResponse.fail(errorCode));
        setResponse(response, status, responseBody);
    }

    /**
     * Sets standardized error response for runtime exceptions
     */
    public void setErrorResponse(HttpServletResponse response, HttpStatus status,
            RuntimeException exception) throws IOException {
        String responseBody = objectMapper.writeValueAsString(
                CommonResponse.fail(exception));
        setResponse(response, status, responseBody);
    }

    private void setResponse(HttpServletResponse response, HttpStatus status, String body) throws IOException {
        response.setCharacterEncoding(UTF_8);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
    }
}
package com.juu.juulabel.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<String>> handle(Exception e) {
        log.error("Exception :", e);
        Sentry.captureException(e);
        return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse<String>> handle(BaseException e) {
        log.error("BaseException :", e);
        Sentry.captureException(e);
        return CommonResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException :", e);
        return CommonResponse.fail(ErrorCode.VALIDATION_ERROR,
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<CommonResponse<String>> handle(CustomJwtException e) {
        log.error("CustomJwtException :", e);
        return CommonResponse.fail(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public void handle(NoResourceFoundException e) {
        log.warn("NoResourceFoundException : {}", e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationException(HttpMessageNotReadableException exception) {
        String errorDetails = "";
        log.error("HttpMessageNotReadableException :", exception);
        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isEnum()) {
                errorDetails = String.format("'%s'. 값은 다음 중 하나여야 합니다: %s.",
                        invalidFormatException.getPath().getLast().getFieldName(),
                        Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
            }
        }
        if (errorDetails.isEmpty()) {
            errorDetails = exception.getMessage();
        }
        return CommonResponse.fail(ErrorCode.VALIDATION_ERROR, errorDetails);
    }
}

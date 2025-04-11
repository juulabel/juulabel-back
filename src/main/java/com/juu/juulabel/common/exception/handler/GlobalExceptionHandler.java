package com.juu.juulabel.common.exception.handler;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse<String>> handle(RuntimeException e) {
        log.error("RuntimeException :", e);
        Sentry.captureException(e);
        return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException :", e);
        return CommonResponse.fail(ErrorCode.VALIDATION_ERROR, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponse<String>> handle(ExpiredJwtException e) {
        log.error("ExpiredJwtException :", e);
        return CommonResponse.fail(ErrorCode.EXPIRED_JWT_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<CommonResponse<String>> handle(MalformedJwtException e) {
        log.error("MalformedJwtException :", e);
        return CommonResponse.fail(ErrorCode.MALFORMED_JWT_EXCEPTION, "잘못된 토큰 형식입니다.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public void handle(NoResourceFoundException e) {
    // 이거 키면 출력이 너무 많이 됨
    //log.warn("NoResourceFoundException : {}", e.getMessage());
    }

}

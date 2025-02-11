package com.juu.juulabel.common.response;


import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record CommonResponse<T>(
        @Schema(description = "성공 여부", example = "true")
        boolean success,
        @Schema(description = "응답 메세지", example = "성공했습니다.")
        String message,
        @Schema(description = "응답 결과")
        T result) {
    public static <T> ResponseEntity<CommonResponse<T>> success(SuccessCode successCode) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new CommonResponse<>(true, successCode.getMessage(), null));
    }

    public static <T> ResponseEntity<CommonResponse<T>> success(SuccessCode successCode, T data) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new CommonResponse<>(true, successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<CommonResponse<T>> fail(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new CommonResponse<>(false, errorCode.getMessage(), null));
    }

    public static <T> ResponseEntity<CommonResponse<T>> fail(ErrorCode errorCode, T data) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new CommonResponse<>(false, errorCode.getMessage(), data));
    }

    public static <T> ResponseEntity<CommonResponse<T>> fail(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse<>(false, exception.getMessage(), null));
    }

}

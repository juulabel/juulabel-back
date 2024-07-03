package com.juu.juulabel.common.response;


import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.exception.code.SuccessCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CommonResponse<T> {
    private final boolean success;
    private final String message;
    private final T result;

    @Builder
    public CommonResponse(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;

    }

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

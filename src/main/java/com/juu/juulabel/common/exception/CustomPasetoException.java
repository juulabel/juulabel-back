package com.juu.juulabel.common.exception;

import com.juu.juulabel.common.exception.code.ErrorCode;

import lombok.Getter;

@Getter
public class CustomPasetoException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomPasetoException(String message) {
        super(message);
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public CustomPasetoException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomPasetoException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}

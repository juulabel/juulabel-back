package com.juu.juulabel.common.exception;

import com.juu.juulabel.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends BaseException {

    public AuthException() {
        super(ErrorCode.HIGH_SECURITY_RISK);
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(String message) {
        super(message, ErrorCode.HIGH_SECURITY_RISK);
    }

    public AuthException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}

package com.juu.juulabel.redis;

import org.springframework.data.redis.RedisSystemException;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.util.HttpResponseUtil;

import io.lettuce.core.RedisCommandExecutionException;

public interface RedisScriptExecutor<T, R> {
    T execute(R arg, Object... args);

    default void handleRedisException(RedisSystemException e) {
        // Check if the cause is a RedisCommandExecutionException
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof RedisCommandExecutionException) {
                handleRedisScriptError(cause.getMessage());
                return;
            }
            cause = cause.getCause();
        }

        // If no RedisCommandExecutionException found, check the main exception message
        handleRedisScriptError(e.getMessage());
    }

    default void handleRedisScriptError(String errorMessage) {
        HttpResponseUtil.addCookie(AuthConstants.REFRESH_TOKEN_HEADER_NAME, "", 0);
        throw new BaseException(errorMessage, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
package com.juu.juulabel.redis;

import org.springframework.data.redis.RedisSystemException;

import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;

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
        throw new AuthException(errorMessage, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
package com.juu.juulabel.auth.executor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import com.juu.juulabel.auth.domain.RefreshToken;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.redis.RedisScriptExecutor;

@Component
public class RotateRefreshTokenScriptExecutor implements RedisScriptExecutor<Object, RefreshToken> {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Object> redisScript;

    public RotateRefreshTokenScriptExecutor(RedisTemplate<String, String> redisTemplate)
            throws IOException {
        this.redisTemplate = redisTemplate;

        String scriptText = Files.readString(
                new ClassPathResource("scripts/rotate_refresh_token.lua").getFile().toPath(), StandardCharsets.UTF_8);
        this.redisScript = RedisScript.of(scriptText, Object.class);
    }

    @Override
    public Object execute(RefreshToken refreshToken, Object... args) {
        String newTokenKey = refreshToken.getTokenKey();
        String indexKey = refreshToken.getIndexKey();
        String oldTokenKey = args[0].toString();

        List<String> keys = Arrays.asList(newTokenKey, indexKey, oldTokenKey);
        List<String> arguments = refreshToken.getArgs();

        try {
            return redisTemplate.execute(redisScript, keys, arguments.toArray());
        } catch (RedisSystemException e) {
            handleRedisException(e);
            throw e; // This line will never be reached due to the exception thrown above
        }
    }

    @Override
    public void handleRedisScriptError(String errorMessage) {
        if (errorMessage.contains("OLD_TOKEN_NOT_FOUND")) {
            throw new AuthException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        } else if (errorMessage.contains("OLD_TOKEN_ALREADY_REVOKED_ALL_TOKENS_INVALIDATED")) {
            throw new AuthException(ErrorCode.REFRESH_TOKEN_REUSE_DETECTED);
        } else if (errorMessage.contains("DEVICE_ID_MISMATCH")) {
            throw new AuthException(ErrorCode.DEVICE_ID_MISMATCH);
        } else {
            throw new AuthException(errorMessage, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
package com.juu.juulabel.auth.executor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import com.juu.juulabel.auth.domain.RefreshToken;

@Component
public class LoginRefreshTokenScriptExecutor implements RedisScriptExecutor<Object, RefreshToken> {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Object> redisScript;

    public LoginRefreshTokenScriptExecutor(RedisTemplate<String, String> redisTemplate) throws IOException {
        this.redisTemplate = redisTemplate;
        String scriptText = Files.readString(
                new ClassPathResource("scripts/login_refresh_token.lua").getFile().toPath(), StandardCharsets.UTF_8);
        this.redisScript = RedisScript.of(scriptText, Object.class);
    }

    @Override
    public Object execute(RefreshToken refreshToken, Object... args) {
        String newTokenKey = refreshToken.getTokenKey();
        String indexKey = refreshToken.getIndexKey();

        List<String> keys = List.of(newTokenKey, indexKey);
        List<String> arguments = refreshToken.getArgs();

        try {
            return redisTemplate.execute(redisScript, keys, arguments.toArray());
        } catch (RedisSystemException e) {
            handleRedisException(e);
            throw e; // This line will never be reached due to the exception thrown above
        }
    }

}

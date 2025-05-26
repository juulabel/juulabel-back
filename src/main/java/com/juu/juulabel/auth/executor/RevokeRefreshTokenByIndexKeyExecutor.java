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

@Component
public class RevokeRefreshTokenByIndexKeyExecutor implements RedisScriptExecutor<Object, String> {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Object> redisScript;

    public RevokeRefreshTokenByIndexKeyExecutor(RedisTemplate<String, String> redisTemplate) throws IOException {
        this.redisTemplate = redisTemplate;
        String scriptText = Files.readString(
                new ClassPathResource("scripts/revoke_refresh_token_by_index_key.lua").getFile().toPath(),
                StandardCharsets.UTF_8);
        this.redisScript = RedisScript.of(scriptText, Object.class);
    }

    @Override
    public Object execute(String indexKey, Object... args) {
        try {
            return redisTemplate.execute(redisScript, List.of(indexKey));
        } catch (RedisSystemException e) {
            handleRedisException(e);
            throw e; // This line will never be reached due to the exception thrown above
        }
    }

}

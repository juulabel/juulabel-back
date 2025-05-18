package com.juu.juulabel.auth.repository.redis;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import com.juu.juulabel.auth.domain.RefreshToken;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_HASH_PREFIX;

import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomRefreshTokenRepositoryImpl implements CustomRefreshTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void revokeByMemberId(Long memberId) {
        revokeByPattern(REFRESH_TOKEN_HASH_PREFIX + ":" + memberId + ":*");
    }

    @Override
    public void revokeByDeviceId(Long memberId, String deviceId) {
        revokeByPattern(REFRESH_TOKEN_HASH_PREFIX + ":" + memberId + ":" + deviceId + ":*");
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(tokenHash))
                .map(RefreshToken.class::cast);
    }

    private void revokeByPattern(String pattern) {
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(1000)
                .build();

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                List<String> keys = new ArrayList<>();
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
                if (!keys.isEmpty()) {
                    redisTemplate.delete(keys);
                }
            } catch (Exception e) {
                throw new BaseException(ErrorCode.REFRESH_TOKEN_INVALID);
            }
            return null;
        });
    }
}
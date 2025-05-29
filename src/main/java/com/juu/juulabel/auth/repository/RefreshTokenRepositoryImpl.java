package com.juu.juulabel.auth.repository;

import com.juu.juulabel.auth.domain.ClientId;
import com.juu.juulabel.auth.domain.RefreshToken;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.redis.RedisScriptName;
import com.juu.juulabel.redis.ScriptRegistry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final ScriptRegistry scriptRegistry;

    @Override
    public void save(RefreshToken refreshToken) {
        scriptRegistry.get(RedisScriptName.SAVE_REFRESH_TOKEN).execute(refreshToken);
    }

    @Override
    public void rotate(RefreshToken refreshToken, String hashedOldToken) {

        String oldTokenKey = AuthConstants.REFRESH_TOKEN_HASH_PREFIX + ":" + hashedOldToken;

        scriptRegistry.get(RedisScriptName.ROTATE_REFRESH_TOKEN).execute(refreshToken, oldTokenKey);
    }

    @Override
    public void login(RefreshToken refreshToken) {
        scriptRegistry.get(RedisScriptName.LOGIN_REFRESH_TOKEN).execute(refreshToken);
    }

    @Override
    public void revokeByMemberAndDevice(Long memberId, ClientId clientId, String deviceId) {
        String indexKey = AuthConstants.REFRESH_TOKEN_INDEX_PREFIX + ":" + memberId + ":" + clientId + ":" + deviceId
                + ":*";

        scriptRegistry.get(RedisScriptName.REVOKE_REFRESH_TOKEN_BY_INDEX_KEY).execute(indexKey);
    }

    @Override
    public void revokeAllByMember(Long memberId) {
        String indexKey = AuthConstants.REFRESH_TOKEN_INDEX_PREFIX + ":" + memberId + ":*";

        scriptRegistry.get(RedisScriptName.REVOKE_REFRESH_TOKEN_BY_INDEX_KEY).execute(indexKey);
    }

}
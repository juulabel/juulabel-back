
package com.juu.juulabel.auth.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.juu.juulabel.auth.domain.RefreshToken;

public interface RefreshTokenRedisRepository
        extends CrudRepository<RefreshToken, String>, CustomRefreshTokenRepository {

}

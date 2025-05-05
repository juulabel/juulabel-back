package com.juu.juulabel.member.repository.jpa;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import com.juu.juulabel.member.domain.RefreshToken;
import com.juu.juulabel.member.repository.RefreshTokenRepository;

@Primary
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepository {
    @Override
    Optional<RefreshToken> findByToken(String token);

    @Override
    boolean existsByParentTokenId(Long parentTokenId);

    @Override
    void deleteByMemberId(Long memberId);
}

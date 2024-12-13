package com.juu.juulabel.domain.repository.jpa;


import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.enums.member.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndDeletedAtIsNull(Long memberId);

    boolean existsByEmailAndProvider(String email, Provider provider);

    boolean existsByNickname(String nickname);
}

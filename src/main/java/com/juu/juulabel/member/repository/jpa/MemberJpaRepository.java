package com.juu.juulabel.member.repository.jpa;


import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndDeletedAtIsNull(Long memberId);

    boolean existsByEmailAndProvider(String email, Provider provider);

    boolean existsByNickname(String nickname);
}

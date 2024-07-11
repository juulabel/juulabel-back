package com.juu.juulabel.domain.repository.jpa;


import com.juu.juulabel.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByProviderId(String providerId);
}

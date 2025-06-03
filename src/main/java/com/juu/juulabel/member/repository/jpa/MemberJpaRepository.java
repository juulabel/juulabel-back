package com.juu.juulabel.member.repository.jpa;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.domain.MemberStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndDeletedAtIsNull(Long memberId);

    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.status != :status")
    Optional<Member> findByEmailAndStatusNot(String email, MemberStatus status);

    boolean existsByEmailAndProvider(String email, Provider provider);

    boolean existsByNickname(String nickname);

}

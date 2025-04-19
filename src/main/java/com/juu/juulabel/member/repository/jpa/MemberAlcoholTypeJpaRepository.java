package com.juu.juulabel.member.repository.jpa;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAlcoholTypeJpaRepository extends JpaRepository<MemberAlcoholType, Long> {
    void deleteByMember(Member member);
}
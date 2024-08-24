package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.MemberAlcoholType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAlcoholTypeJpaRepository extends JpaRepository<MemberAlcoholType, Long> {
    void deleteByMember(Member member);
}
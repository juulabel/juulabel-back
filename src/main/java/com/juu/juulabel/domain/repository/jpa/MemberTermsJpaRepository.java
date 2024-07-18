package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.member.MemberTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermsJpaRepository extends JpaRepository<MemberTerms, Long> {
}
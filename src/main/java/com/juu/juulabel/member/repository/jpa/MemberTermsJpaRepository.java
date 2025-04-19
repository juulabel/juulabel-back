package com.juu.juulabel.member.repository.jpa;

import com.juu.juulabel.member.domain.MemberTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermsJpaRepository extends JpaRepository<MemberTerms, Long> {
}
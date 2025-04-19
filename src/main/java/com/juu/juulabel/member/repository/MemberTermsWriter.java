package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.member.domain.MemberTerms;
import com.juu.juulabel.member.repository.jpa.MemberTermsJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Writer
@RequiredArgsConstructor
public class MemberTermsWriter {

    private final MemberTermsJpaRepository memberTermsJpaRepository;

    public List<MemberTerms> storeAll(List<MemberTerms> memberTermsList) {
        return memberTermsJpaRepository.saveAll(memberTermsList);
    }

}
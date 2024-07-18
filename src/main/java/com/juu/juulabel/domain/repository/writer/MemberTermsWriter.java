package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.member.MemberTerms;
import com.juu.juulabel.domain.repository.jpa.MemberTermsJpaRepository;
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
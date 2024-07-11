package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.MemberJpaRepository;
import com.juu.juulabel.domain.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class MemberReader {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public Member getByProviderId(String providerId) {
        return memberQueryRepository.getByProviderId(providerId);
    }

    public boolean existsByProviderId(String providerId) {
        return memberJpaRepository.existsByProviderId(providerId);
    }
}

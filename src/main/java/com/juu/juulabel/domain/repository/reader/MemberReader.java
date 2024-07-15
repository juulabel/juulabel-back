package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.enums.Provider;
import com.juu.juulabel.domain.repository.jpa.MemberJpaRepository;
import com.juu.juulabel.domain.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class MemberReader {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public boolean existsByEmailAndProvider(String email, Provider provider) {
        return memberJpaRepository.existsByEmailAndProvider(email, provider);
    }

    public boolean existActiveEmail(String email) {
        return memberQueryRepository.existActiveEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }
}

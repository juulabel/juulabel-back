package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.MemberJpaRepository;
import com.juu.juulabel.domain.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class MemberWriter {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public void store(Member member) {
        memberJpaRepository.save(member);
    }

    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }
}

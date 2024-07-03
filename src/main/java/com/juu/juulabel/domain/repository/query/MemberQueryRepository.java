package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final MemberJpaRepository memberJpaRepository;

    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public boolean existsByNickname(String nickName) {
        return memberJpaRepository.existsByNickname(nickName);
    }

}

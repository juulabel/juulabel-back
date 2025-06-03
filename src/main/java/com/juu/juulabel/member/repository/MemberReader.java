package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.repository.jpa.MemberJpaRepository;
import com.juu.juulabel.member.repository.jpa.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class MemberReader {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public Member getById(final Long id) {
        return memberJpaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Cacheable(value = "member", key = "#email")
    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Optional<Member> getOptionalByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    public boolean existsByEmailAndProvider(String email, Provider provider) {
        return memberJpaRepository.existsByEmailAndProvider(email, provider);
    }

    public boolean existActiveEmail(String email) {
        return memberQueryRepository.existActiveEmail(email);
    }

    public boolean existActiveNickname(String nickname) {
        return memberQueryRepository.existActiveNickname(nickname);
    }

    public List<Member> getActiveMembers() {
        return memberQueryRepository.getActiveMembers();
    }
}

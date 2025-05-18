package com.juu.juulabel.member.service;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.repository.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 조회 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberLookupService {

    private final MemberReader memberReader;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * ID로 회원 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "memberById", key = "#memberId", unless = "#result == null")
    public Member findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 이메일로 회원 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "memberByEmail", key = "#email", unless = "#result == null")
    public Member getMemberByEmail(String email) {
        return memberReader.getByEmail(email);
    }
}
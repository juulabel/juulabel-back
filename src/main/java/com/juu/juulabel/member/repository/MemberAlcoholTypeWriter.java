package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholType;
import com.juu.juulabel.member.repository.jpa.MemberAlcoholTypeJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Writer
@RequiredArgsConstructor
public class MemberAlcoholTypeWriter {

    private final MemberAlcoholTypeJpaRepository memberAlcoholTypeJpaRepository;

    public List<MemberAlcoholType> storeAll(List<MemberAlcoholType> memberAlcoholTypeList) {
        return memberAlcoholTypeJpaRepository.saveAll(memberAlcoholTypeList);
    }

    public void deleteAllByMember(Member member) {
        memberAlcoholTypeJpaRepository.deleteByMember(member);
    }

}
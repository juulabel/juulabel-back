package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.MemberAlcoholType;
import com.juu.juulabel.domain.repository.jpa.MemberAlcoholTypeJpaRepository;
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
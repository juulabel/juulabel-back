package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.repository.query.MemberAlcoholTypeQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class MemberAlcoholTypeReader {

    private final MemberAlcoholTypeQueryRepository memberAlcoholTypeQueryRepository;

    public List<Long> getIdListByMember(Member member) {
        List<Long> alcoholTypeIdList = memberAlcoholTypeQueryRepository.findAlcoholTypeIdsByMember(member);
        return Optional.ofNullable(alcoholTypeIdList)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.ALCOHOL_TYPE_NOT_FOUND));
    }
}
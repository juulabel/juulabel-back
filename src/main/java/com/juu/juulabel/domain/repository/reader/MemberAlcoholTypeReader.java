package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.query.MemberAlcoholTypeQueryRepository;
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
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_ALCOHOL_TYPE));
    }
}
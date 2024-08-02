package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeSummary;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeJpaRepository;
import com.juu.juulabel.domain.repository.query.DailyLifeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class DailyLifeReader {

    private final DailyLifeQueryRepository dailyLifeQueryRepository;
    private final DailyLifeJpaRepository dailyLifeJpaRepository;

    public DailyLifeDetailInfo getDailyLifeDetailById(final Long dailyLifeId, final Member member) {
        return dailyLifeQueryRepository.getDailyLifeDetailById(dailyLifeId, member);
    }

    public DailyLife getById(final Long dailyLifeId) {
        return dailyLifeJpaRepository.findByIdAndDeletedAtIsNull(dailyLifeId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_DAILY_LIFE));
    }

    public Slice<DailyLifeSummary> getAllDailyLife(final Member member,
                                                   final Long lastDailyLifeId,
                                                   final int pageSize) {
        return dailyLifeQueryRepository.getAllDailyLife(member, lastDailyLifeId, pageSize);
    }
}

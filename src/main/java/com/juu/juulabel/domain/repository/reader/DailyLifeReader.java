package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.repository.jpa.DailyLifeJpaRepository;
import com.juu.juulabel.domain.repository.query.DailyLifeQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class DailyLifeReader {

    private final DailyLifeQueryRepository dailyLifeQueryRepository;
    private final DailyLifeJpaRepository dailyLifeJpaRepository;

    public DailyLifeDetailInfo getDailyLifeDetailById(final Long dailyLifeId) {
        return dailyLifeQueryRepository.getDailyLifeDetailById(dailyLifeId);
    }

    public DailyLife getById(final Long dailyLifeId) {
        return dailyLifeJpaRepository.findByIdAndDeletedAtIsNull(dailyLifeId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_DAILY_LIFE));
    }
}

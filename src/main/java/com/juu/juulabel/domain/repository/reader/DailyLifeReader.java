package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.repository.query.DailyLifeQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class DailyLifeReader {

    private final DailyLifeQueryRepository dailyLifeQueryRepository;

    public DailyLifeDetailInfo getDailyLifeDetailById(final Long dailyLifeId) {
        return dailyLifeQueryRepository.getDailyLifeDetailById(dailyLifeId);
    }

}

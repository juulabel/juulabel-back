package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.repository.jpa.DailyLifeCommentJpaRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class DailyLifeCommentReader {

    private final DailyLifeCommentJpaRepository dailyLifeCommentJpaRepository;

    public DailyLifeComment getById(final Long dailyLifeCommentId) {
        return dailyLifeCommentJpaRepository.findByIdAndDeletedAtIsNull(dailyLifeCommentId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_DAILY_LIFE_COMMENT));
    }

}
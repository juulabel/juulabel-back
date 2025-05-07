package com.juu.juulabel.dailylife.service;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.dailylife.domain.DailyLifeComment;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeCommentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyLifeCommentService {
    private final DailyLifeCommentJpaRepository dailyLifeCommentJpaRepository;

    public DailyLifeComment findById(long id) {
        return dailyLifeCommentJpaRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.DAILY_LIFE_COMMENT_NOT_FOUND));
    }
}


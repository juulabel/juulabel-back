package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.dailylife.domain.DailyLifeComment;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeCommentJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DailyLifeCommentWriter {

    private final DailyLifeCommentJpaRepository dailyLifeCommentJpaRepository;

    public DailyLifeComment store(final DailyLifeComment comment) {
        return dailyLifeCommentJpaRepository.save(comment);
    }

}
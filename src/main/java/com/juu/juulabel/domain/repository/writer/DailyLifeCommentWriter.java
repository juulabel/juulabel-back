package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.repository.jpa.DailyLifeCommentJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DailyLifeCommentWriter {

    private final DailyLifeCommentJpaRepository dailyLifeCommentJpaRepository;

    public DailyLifeComment store(final DailyLifeComment comment) {
        return dailyLifeCommentJpaRepository.save(comment);
    }

}
package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.dailylife.domain.DailyLifeComment;
import com.juu.juulabel.dailylife.domain.DailyLifeCommentLike;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeCommentLikeJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DailyLifeCommentLikeWriter {

    private final DailyLifeCommentLikeJpaRepository dailyLifeCommentLikeJpaRepository;

    public void store(final Member member, final DailyLifeComment dailyLifeComment) {
        final DailyLifeCommentLike dailyLifeCommentLike = DailyLifeCommentLike.create(member, dailyLifeComment);
        dailyLifeCommentLikeJpaRepository.save(dailyLifeCommentLike);
    }

    public void delete(final DailyLifeCommentLike dailyLifeCommentLike) {
        dailyLifeCommentLikeJpaRepository.delete(dailyLifeCommentLike);
    }

}
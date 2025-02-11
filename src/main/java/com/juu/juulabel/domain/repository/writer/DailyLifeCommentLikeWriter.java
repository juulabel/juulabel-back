package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeCommentLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeCommentLikeJpaRepository;
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
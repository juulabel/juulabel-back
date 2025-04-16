package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeCommentLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeCommentLikeJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class DailyLifeCommentLikeReader {

    private final DailyLifeCommentLikeJpaRepository dailyLifeCommentLikeJpaRepository;

    public Optional<DailyLifeCommentLike> findByMemberAndDailyLifeComment(Member member, DailyLifeComment dailyLifeComment) {
        return dailyLifeCommentLikeJpaRepository.findByMemberAndDailyLifeComment(member, dailyLifeComment);
    }
}
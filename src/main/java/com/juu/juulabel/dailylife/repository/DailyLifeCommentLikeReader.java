package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.dailylife.domain.DailyLifeComment;
import com.juu.juulabel.dailylife.domain.DailyLifeCommentLike;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeCommentLikeJpaRepository;
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
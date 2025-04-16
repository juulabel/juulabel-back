package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeCommentLike;
import com.juu.juulabel.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeCommentLikeJpaRepository extends JpaRepository<DailyLifeCommentLike, Long> {
    Optional<DailyLifeCommentLike> findByMemberAndDailyLifeComment(Member member, DailyLifeComment dailyLifeComment);
}
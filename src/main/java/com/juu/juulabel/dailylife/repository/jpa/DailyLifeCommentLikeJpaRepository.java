package com.juu.juulabel.dailylife.repository.jpa;

import com.juu.juulabel.dailylife.domain.DailyLifeComment;
import com.juu.juulabel.dailylife.domain.DailyLifeCommentLike;
import com.juu.juulabel.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeCommentLikeJpaRepository extends JpaRepository<DailyLifeCommentLike, Long> {
    Optional<DailyLifeCommentLike> findByMemberAndDailyLifeComment(Member member, DailyLifeComment dailyLifeComment);
}
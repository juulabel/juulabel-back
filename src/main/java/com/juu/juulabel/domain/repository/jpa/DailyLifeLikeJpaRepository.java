package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeLike;
import com.juu.juulabel.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeLikeJpaRepository extends JpaRepository<DailyLifeLike, Long> {
    Optional<DailyLifeLike> findByMemberAndDailyLife(Member member, DailyLife dailyLife);
}
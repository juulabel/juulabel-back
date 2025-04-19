package com.juu.juulabel.dailylife.repository.jpa;

import com.juu.juulabel.dailylife.domain.DailyLife;
import com.juu.juulabel.dailylife.domain.DailyLifeLike;
import com.juu.juulabel.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeLikeJpaRepository extends JpaRepository<DailyLifeLike, Long> {
    Optional<DailyLifeLike> findByMemberAndDailyLife(Member member, DailyLife dailyLife);
}
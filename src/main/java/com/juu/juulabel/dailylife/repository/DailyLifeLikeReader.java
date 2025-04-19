package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.dailylife.domain.DailyLife;
import com.juu.juulabel.dailylife.domain.DailyLifeLike;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeLikeJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class DailyLifeLikeReader {

    private final DailyLifeLikeJpaRepository dailyLifeLikeJpaRepository;


    public Optional<DailyLifeLike> findByMemberAndDailyLife(Member member, DailyLife dailyLife) {
        return dailyLifeLikeJpaRepository.findByMemberAndDailyLife(member, dailyLife);
    }
}
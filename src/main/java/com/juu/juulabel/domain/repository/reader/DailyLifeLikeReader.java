package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeLikeJpaRepository;
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
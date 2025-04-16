package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.dailylife.domain.DailyLife;
import com.juu.juulabel.dailylife.domain.DailyLifeLike;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeLikeJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DailyLifeLikeWriter {

    private final DailyLifeLikeJpaRepository dailyLifeLikeJpaRepository;

    public void store(final Member member, final DailyLife dailyLife) {
        final DailyLifeLike dailyLifeLike = DailyLifeLike.create(member, dailyLife);
        dailyLifeLikeJpaRepository.save(dailyLifeLike);
    }

    public void delete(final DailyLifeLike dailyLifeLike) {
        dailyLifeLikeJpaRepository.delete(dailyLifeLike);
    }
}

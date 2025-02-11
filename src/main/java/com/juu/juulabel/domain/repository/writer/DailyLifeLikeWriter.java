package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeLikeJpaRepository;
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

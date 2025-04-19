package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.dto.request.WriteDailyLifeRequest;
import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.dailylife.domain.DailyLife;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DailyLifeWriter {

    private final DailyLifeJpaRepository dailyLifeJpaRepository;

    public DailyLife store(final Member member, final WriteDailyLifeRequest request) {
        final DailyLife dailyLife = DailyLife.create(member, request.title(), request.content(), request.isPrivate());
        return dailyLifeJpaRepository.save(dailyLife);
    }
}

package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.api.dto.request.WriteDailyLifeRequest;
import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeJpaRepository;
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

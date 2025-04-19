package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.dailylife.domain.DailyLife;
import com.juu.juulabel.dailylife.domain.DailyLifeImage;
import com.juu.juulabel.dailylife.repository.jpa.DailyLifeImageJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DailyLifeImageWriter {

    private final DailyLifeImageJpaRepository dailyLifeImageJpaRepository;

    public DailyLifeImage store(final DailyLife dailyLife, final int seq, final String imagePath) {
        final DailyLifeImage dailyLifeImage = DailyLifeImage.create(dailyLife, seq, imagePath);
        return dailyLifeImageJpaRepository.save(dailyLifeImage);
    }

}

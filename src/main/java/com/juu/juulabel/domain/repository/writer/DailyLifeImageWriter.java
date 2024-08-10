package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeImage;
import com.juu.juulabel.domain.repository.jpa.DailyLifeImageJpaRepository;
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

package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeImage;
import com.juu.juulabel.domain.repository.query.DailyLifeImageQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class DailyLifeImageReader {

    private final DailyLifeImageQueryRepository dailyLifeImageQueryRepository;

    public List<String> getImageUrlList(final Long dailyLifeId) {
        return dailyLifeImageQueryRepository.getImageUrlList(dailyLifeId);
    }

    public List<DailyLifeImage> getImageList(final Long dailyLifeId) {
        return dailyLifeImageQueryRepository.getImageList(dailyLifeId);
    }

}

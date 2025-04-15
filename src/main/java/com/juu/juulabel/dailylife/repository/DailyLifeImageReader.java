package com.juu.juulabel.dailylife.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.dailylife.domain.DailyLifeImage;
import com.juu.juulabel.dailylife.repository.query.DailyLifeImageQueryRepository;
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

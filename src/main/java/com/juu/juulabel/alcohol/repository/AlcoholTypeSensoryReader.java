package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.response.SensoryLevelInfo;
import com.juu.juulabel.alcohol.domain.SensoryLevel;
import com.juu.juulabel.alcohol.repository.jpa.AlcoholTypeSensoryJpaRepository;
import com.juu.juulabel.alcohol.repository.query.AlcoholTypeSensoryQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeSensoryReader {

    private final AlcoholTypeSensoryJpaRepository alcoholTypeSensoryJpaRepository;
    private final AlcoholTypeSensoryQueryRepository alcoholTypeSensoryQueryRepository;

    public List<SensoryLevel> getAllSensoryLevelByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeSensoryJpaRepository.findAllSensoryLevelByAlcoholTypeId(alcoholTypeId);
    }

    public List<SensoryLevelInfo> getAllSensoryLevelInfoByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeSensoryQueryRepository.findAllInfoByAlcoholTypeId(alcoholTypeId);
    }

}

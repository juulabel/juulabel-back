package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.SensoryLevelInfo;
import com.juu.juulabel.domain.entity.alcohol.SensoryLevel;
import com.juu.juulabel.domain.repository.jpa.AlcoholTypeSensoryJpaRepository;
import com.juu.juulabel.domain.repository.query.AlcoholTypeSensoryQueryRepository;
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

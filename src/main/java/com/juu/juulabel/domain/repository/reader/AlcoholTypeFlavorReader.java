package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.FlavorLevelInfo;
import com.juu.juulabel.domain.entity.alcohol.FlavorLevel;
import com.juu.juulabel.domain.repository.jpa.AlcoholTypeFlavorJpaRepository;
import com.juu.juulabel.domain.repository.query.AlcoholTypeFlavorQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeFlavorReader {

    private final AlcoholTypeFlavorJpaRepository alcoholTypeFlavorJpaRepository;
    private final AlcoholTypeFlavorQueryRepository alcoholTypeFlavorQueryRepository;

    public List<FlavorLevel> getAllFlavorLevelByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeFlavorJpaRepository.findAllSensoryLevelByAlcoholTypeId(alcoholTypeId);
    }

    public List<FlavorLevelInfo> getAllFlavorLevelInfoByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeFlavorQueryRepository.findAllInfoByAlcoholTypeId(alcoholTypeId);
    }

}

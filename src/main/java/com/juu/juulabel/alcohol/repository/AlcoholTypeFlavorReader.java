package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.request.FlavorLevelInfo;
import com.juu.juulabel.alcohol.domain.FlavorLevel;
import com.juu.juulabel.alcohol.repository.jpa.AlcoholTypeFlavorJpaRepository;
import com.juu.juulabel.alcohol.repository.query.AlcoholTypeFlavorQueryRepository;
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

package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import com.juu.juulabel.domain.repository.query.AlcoholTypeSensoryQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeSensoryReader {

    private final AlcoholTypeSensoryQueryRepository alcoholTypeSensoryQueryRepository;

    public List<SensoryType> getAllSensoryTypesByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeSensoryQueryRepository.findAllSensoryTypesByAlcoholTypeId(alcoholTypeId);
    }

}

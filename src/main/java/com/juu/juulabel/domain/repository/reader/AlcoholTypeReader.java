package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.UsedAlcoholTypeInfo;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.repository.jpa.AlcoholTypeRepository;
import com.juu.juulabel.domain.repository.query.AlcoholTypeQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeReader {

    private final AlcoholTypeRepository alcoholTypeRepository;
    private final AlcoholTypeQueryRepository alcoholTypeQueryRepository;

    public List<UsedAlcoholTypeInfo> getAllUsedAlcoholType() {
        return alcoholTypeQueryRepository.getAllUsedAlcoholType();
    }

    public AlcoholType getById(Long alcoholTypeId) {
        return alcoholTypeQueryRepository.getById(alcoholTypeId);
    }

}

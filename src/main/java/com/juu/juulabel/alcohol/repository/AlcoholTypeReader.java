package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.request.UsedAlcoholTypeInfo;
import com.juu.juulabel.alcohol.domain.AlcoholType;
import com.juu.juulabel.alcohol.repository.jpa.AlcoholTypeJpaRepository;
import com.juu.juulabel.alcohol.repository.query.AlcoholTypeQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeReader {

    private final AlcoholTypeJpaRepository alcoholTypeJpaRepository;
    private final AlcoholTypeQueryRepository alcoholTypeQueryRepository;

    public List<UsedAlcoholTypeInfo> getAllUsedAlcoholType() {
        return alcoholTypeQueryRepository.getAllUsedAlcoholType();
    }

    public AlcoholType getById(Long alcoholTypeId) {
        return alcoholTypeQueryRepository.getById(alcoholTypeId);
    }

}

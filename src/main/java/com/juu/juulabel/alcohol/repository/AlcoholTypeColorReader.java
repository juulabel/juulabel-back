package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.request.ColorInfo;
import com.juu.juulabel.alcohol.domain.Color;
import com.juu.juulabel.alcohol.repository.jpa.AlcoholTypeColorJpaRepository;
import com.juu.juulabel.alcohol.repository.query.AlcoholTypeColorQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeColorReader {

    private final AlcoholTypeColorJpaRepository alcoholTypeColorJpaRepository;
    private final AlcoholTypeColorQueryRepository alcoholTypeColorQueryRepository;

    public List<Color> getAllColorByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeColorJpaRepository.findAllByAlcoholTypeId(alcoholTypeId);
    }

    public List<ColorInfo> getAllColorInfoByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeColorQueryRepository.findAllByAlcoholTypeId(alcoholTypeId);
    }

}

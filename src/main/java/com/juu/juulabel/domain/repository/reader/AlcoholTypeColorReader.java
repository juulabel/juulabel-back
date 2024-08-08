package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.ColorInfo;
import com.juu.juulabel.domain.entity.alcohol.Color;
import com.juu.juulabel.domain.repository.jpa.AlcoholTypeColorJpaRepository;
import com.juu.juulabel.domain.repository.query.AlcoholTypeColorQueryRepository;
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

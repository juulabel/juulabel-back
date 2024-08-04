package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.ColorInfo;
import com.juu.juulabel.domain.repository.query.AlcoholTypeColorQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeColorReader {

    private final AlcoholTypeColorQueryRepository alcoholTypeColorQueryRepository;

    public List<ColorInfo> getAllByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeColorQueryRepository.findAllByAlcoholTypeId(alcoholTypeId);
    }

}

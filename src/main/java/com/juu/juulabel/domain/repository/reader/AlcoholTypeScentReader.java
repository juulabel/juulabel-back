package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.CategoryWithScentSummary;
import com.juu.juulabel.domain.repository.query.AlcoholTypeScentQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeScentReader {

    private final AlcoholTypeScentQueryRepository alcoholTypeScentQueryRepository;

    public List<CategoryWithScentSummary> getAllCategoryWithScentByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeScentQueryRepository.findAllCategoryWithScentByAlcoholTypeId(alcoholTypeId);
    }

}

package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.response.CategoryWithScentSummary;
import com.juu.juulabel.alcohol.domain.Scent;
import com.juu.juulabel.alcohol.repository.jpa.AlcoholTypeScentJpaRepository;
import com.juu.juulabel.alcohol.repository.query.AlcoholTypeScentQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AlcoholTypeScentReader {

    private final AlcoholTypeScentJpaRepository alcoholTypeScentJpaRepository;
    private final AlcoholTypeScentQueryRepository alcoholTypeScentQueryRepository;

    public List<Scent> getAllScentByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeScentJpaRepository.findAllByAlcoholTypeId(alcoholTypeId);
    }

    public List<CategoryWithScentSummary> getAllCategoryWithScentByAlcoholTypeId(Long alcoholTypeId) {
        return alcoholTypeScentQueryRepository.findAllCategoryWithScentByAlcoholTypeId(alcoholTypeId);
    }

}

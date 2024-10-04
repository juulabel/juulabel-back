package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicBrewerySummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import com.juu.juulabel.domain.repository.query.BreweryQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class BreweryReader {

    private final BreweryQueryRepository breweryQueryRepository;

    public BrewerySummary getBreweryById(Long breweryId) {
        return breweryQueryRepository.getBreweryById(breweryId);
    }
}
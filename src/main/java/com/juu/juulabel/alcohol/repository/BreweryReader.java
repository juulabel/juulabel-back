package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.response.BrewerySummary;
import com.juu.juulabel.alcohol.repository.query.BreweryQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class BreweryReader {

    private final BreweryQueryRepository breweryQueryRepository;

    public BrewerySummary getBreweryById(Long breweryId) {
        return breweryQueryRepository.getBreweryById(breweryId);
    }
}
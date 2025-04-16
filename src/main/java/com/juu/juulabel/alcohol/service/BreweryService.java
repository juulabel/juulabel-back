package com.juu.juulabel.alcohol.service;

import com.juu.juulabel.common.dto.response.BreweryDetailResponse;
import com.juu.juulabel.alcohol.response.AlcoholicBrewerySummary;
import com.juu.juulabel.alcohol.response.BrewerySummary;
import com.juu.juulabel.alcohol.repository.AlcoholicDrinksReader;
import com.juu.juulabel.alcohol.repository.BreweryReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BreweryService {

    private final BreweryReader breweryReader;
    private final AlcoholicDrinksReader alcoholicDrinksReader;

    // 양조장 상세보기
    @Transactional(readOnly = true)
        public BreweryDetailResponse loadBreweryDetail(final long breweryId){

        List<AlcoholicBrewerySummary> alcoholicBrewerySummary = alcoholicDrinksReader.getAllByBreweryId(breweryId);

        BrewerySummary brewerySummary = breweryReader.getBreweryById(breweryId);

            return new BreweryDetailResponse(
                    brewerySummary,
                    alcoholicBrewerySummary
            );
        }
    }

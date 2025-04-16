package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.response.BreweryDetailResponse;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicBrewerySummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import com.juu.juulabel.domain.repository.reader.AlcoholicDrinksReader;
import com.juu.juulabel.domain.repository.reader.BreweryReader;
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

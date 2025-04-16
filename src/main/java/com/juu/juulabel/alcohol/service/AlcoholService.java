package com.juu.juulabel.alcohol.service;

import com.juu.juulabel.common.dto.response.AlcoholTypeListResponse;
import com.juu.juulabel.common.dto.response.AlcoholicDrinksListWithSizeResponse;
import com.juu.juulabel.common.dto.response.FlavorListResponse;
import com.juu.juulabel.common.dto.response.SensoryListResponse;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.alcohol.request.UsedAlcoholTypeInfo;
import com.juu.juulabel.alcohol.request.UsedFlavorInfo;
import com.juu.juulabel.alcohol.request.UsedSensoryInfo;
import com.juu.juulabel.alcohol.repository.AlcoholTypeReader;
import com.juu.juulabel.alcohol.repository.AlcoholicDrinksReader;
import com.juu.juulabel.alcohol.repository.FlavorReader;
import com.juu.juulabel.alcohol.repository.SensoryReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlcoholService {

    private final AlcoholTypeReader alcoholTypeReader;
    private final SensoryReader sensoryReader;
    private final FlavorReader flavorReader;
    private final AlcoholicDrinksReader alcoholicDrinksReader;

    public AlcoholTypeListResponse loadUsedAlcoholTypeList() {
        List<UsedAlcoholTypeInfo> alcoholTypeInfoList = alcoholTypeReader.getAllUsedAlcoholType();
        return new AlcoholTypeListResponse(alcoholTypeInfoList);
    }

    public SensoryListResponse loadUsedSensoryList() {
        List<UsedSensoryInfo> sensoryInfoList = sensoryReader.getAllUsedSensory();
        return new SensoryListResponse(sensoryInfoList);
    }

    public FlavorListResponse loadUsedFlavorList() {
        List<UsedFlavorInfo> flavorInfoList = flavorReader.getAllUsedFlavor();
        return new FlavorListResponse(flavorInfoList);
    }

    public AlcoholicDrinksListWithSizeResponse loadAlcoholicDrinksList(int size) {
        if (size > 809) throw new InvalidParamException();
        return alcoholicDrinksReader.loadAlcoholicDrinksList(size);
    }
}
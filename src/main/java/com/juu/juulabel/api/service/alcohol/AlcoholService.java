package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.response.AlcoholTypeListResponse;
import com.juu.juulabel.api.dto.response.SensoryListResponse;
import com.juu.juulabel.domain.dto.alcohol.UsedAlcoholTypeInfo;
import com.juu.juulabel.domain.dto.alcohol.UsedSensoryInfo;
import com.juu.juulabel.domain.repository.reader.AlcoholTypeReader;
import com.juu.juulabel.domain.repository.reader.SensoryReader;
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

    public AlcoholTypeListResponse loadUsedAlcoholTypeList() {
        List<UsedAlcoholTypeInfo> alcoholTypeInfoList = alcoholTypeReader.getAllUsedAlcoholType();
        return new AlcoholTypeListResponse(alcoholTypeInfoList);
    }

    public SensoryListResponse loadUsedSensoryList() {
        List<UsedSensoryInfo> sensoryInfoList = sensoryReader.getAllUsedSensory();
        return new SensoryListResponse(sensoryInfoList);
    }
}
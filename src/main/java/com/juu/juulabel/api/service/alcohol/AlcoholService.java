package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.response.AlcoholTypeListResponse;
import com.juu.juulabel.domain.dto.alcohol.UsedAlcoholTypeInfo;
import com.juu.juulabel.domain.repository.reader.AlcoholTypeReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlcoholService {

    private final AlcoholTypeReader alcoholTypeReader;


    public AlcoholTypeListResponse loadUsedAlcoholTypeList() {
        List<UsedAlcoholTypeInfo> alcoholTypeInfoList = alcoholTypeReader.getAllUsedAlcoholType();
        return new AlcoholTypeListResponse(alcoholTypeInfoList);
    }
}
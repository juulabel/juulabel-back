package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.response.UsedSensoryInfo;
import com.juu.juulabel.alcohol.repository.query.SensoryQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class SensoryReader {

    private final SensoryQueryRepository sensoryQueryRepository;

    public List<UsedSensoryInfo> getAllUsedSensory() {
        return sensoryQueryRepository.getAllUsedSensory();
    }

}

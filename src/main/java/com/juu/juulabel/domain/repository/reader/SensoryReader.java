package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.UsedSensoryInfo;
import com.juu.juulabel.domain.repository.query.SensoryQueryRepository;
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

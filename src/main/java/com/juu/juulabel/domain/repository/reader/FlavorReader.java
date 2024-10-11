package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.UsedFlavorInfo;
import com.juu.juulabel.domain.repository.query.FlavorQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class FlavorReader {

    private final FlavorQueryRepository flavorQueryRepository;

    public List<UsedFlavorInfo> getAllUsedFlavor() {
        return flavorQueryRepository.getAllUsedFlavor();
    }
}

package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.request.UsedFlavorInfo;
import com.juu.juulabel.alcohol.repository.query.FlavorQueryRepository;
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

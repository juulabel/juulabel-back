package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.UsedFlavorInfo;

import java.util.List;

public record FlavorListResponse(
    List<UsedFlavorInfo> flavorInfos
) {
}

package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.request.UsedFlavorInfo;

import java.util.List;

public record FlavorListResponse(
    List<UsedFlavorInfo> flavorInfos
) {
}

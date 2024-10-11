package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.UsedSensoryInfo;

import java.util.List;

public record SensoryListResponse(
    List<UsedSensoryInfo> sensoryInfos
) {
}

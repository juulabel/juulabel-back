package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.request.UsedSensoryInfo;

import java.util.List;

public record SensoryListResponse(
    List<UsedSensoryInfo> sensoryInfos
) {
}

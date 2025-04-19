package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.response.UsedSensoryInfo;

import java.util.List;

public record SensoryListResponse(
    List<UsedSensoryInfo> sensoryInfos
) {
}

package com.juu.juulabel.domain.dto.alcohol;

public record UsedSensoryInfo(
    Long sensoryId,
    String sensoryName,
    Long sensoryLevelId,
    String description,
    int score
) {
}

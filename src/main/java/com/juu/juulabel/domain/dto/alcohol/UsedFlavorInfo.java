package com.juu.juulabel.domain.dto.alcohol;

public record UsedFlavorInfo(
    Long flavorId,
    String flavorName,
    Long flavorLevelId,
    String description,
    int score
) {
}

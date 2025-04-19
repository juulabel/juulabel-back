package com.juu.juulabel.alcohol.response;

public record UsedFlavorInfo(
    Long flavorId,
    String flavorName,
    Long flavorLevelId,
    String description,
    int score
) {
}

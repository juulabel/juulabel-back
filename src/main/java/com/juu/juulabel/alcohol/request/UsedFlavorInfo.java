package com.juu.juulabel.alcohol.request;

public record UsedFlavorInfo(
    Long flavorId,
    String flavorName,
    Long flavorLevelId,
    String description,
    int score
) {
}

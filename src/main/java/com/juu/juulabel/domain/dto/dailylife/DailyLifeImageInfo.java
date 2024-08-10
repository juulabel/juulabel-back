package com.juu.juulabel.domain.dto.dailylife;

import java.util.List;

public record DailyLifeImageInfo(
    List<String> imageUrlList,
    int imageCount
) {
}

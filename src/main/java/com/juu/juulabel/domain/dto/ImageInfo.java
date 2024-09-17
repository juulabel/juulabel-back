package com.juu.juulabel.domain.dto;

import java.util.List;

public record ImageInfo(
    List<String> imageUrlList,
    int imageCount
) {
}

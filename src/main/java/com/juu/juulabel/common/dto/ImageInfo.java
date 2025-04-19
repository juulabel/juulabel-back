package com.juu.juulabel.common.dto;

import java.util.List;

public record ImageInfo(
    List<String> imageUrlList,
    int imageCount
) {
}

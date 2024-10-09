package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "촉각 정보")
public record SensoryInfo(
        @Schema(description = "촉각 고유 번호", example = "1")
        Long id,
        @Schema(description = "촉각 이름(한국어)", example = "탄산도")
        String name,
        @Schema(description = "촉각 설명", example = "술의 탄산이 얼마나 느껴지나요?")
        String description
) {
}

package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

public record SensoryDetail(
        @Schema(description = "촉각 이름")
        String name,
        @Schema(description = "촉각 점수")
        int score,
        @Schema(description = "촉각 ID")
        Long id
) {
}

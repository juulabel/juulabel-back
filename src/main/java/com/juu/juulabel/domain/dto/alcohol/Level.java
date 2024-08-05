package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "촉각 레벨")
public record Level(
        @Schema(description = "코드(벨류)", example = "MEDIUM")
        String name,
        @Schema(description = "점수", example = "1")
        int score,
        @Schema(description = "설명", example = "맑음")
        String description
) {
}

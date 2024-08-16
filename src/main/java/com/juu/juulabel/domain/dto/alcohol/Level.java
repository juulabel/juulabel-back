package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "촉각 레벨")
public record Level(
        @Schema(description = "고유 번호", example = "4")
        Long id,
        @Schema(description = "점수", example = "1")
        Integer score,
        @Schema(description = "설명", example = "맑음")
        String description
) {
}

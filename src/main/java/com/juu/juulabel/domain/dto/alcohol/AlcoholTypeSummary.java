package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주종 간단 정보")
public record AlcoholTypeSummary(
        @Schema(description = "주종 고유 번호", example = "38")
        Long id,
        @Schema(description = "주종명", example = "소주+증류주")
        String name
) {
}

package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "원재료 정보")
public record IngredientSummary(
        @Schema(description = "원재료 고유 번호")
        Long id,
        @Schema(description = "원재료 명")
        String name
) {
}

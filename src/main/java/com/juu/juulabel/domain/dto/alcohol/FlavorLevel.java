package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "각 촉각 타입에 대한 점수와 설명 리스트")
public record FlavorLevel(
        @Schema(description = "미각 타입", example = "SWEETNESS (SWEETNESS, SOURNESS, BITTERNESS, UMAMI, AFTERTASTE, BODY)")
        FlavorTypeInfo flavorType,
        @Schema(description = "미각의 점수와 설명 리스트")
        List<Level> levels
) {
}

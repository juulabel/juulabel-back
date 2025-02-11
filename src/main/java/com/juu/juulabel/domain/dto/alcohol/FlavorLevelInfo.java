package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "각 미각 타입에 대한 점수와 설명 리스트")
public record FlavorLevelInfo(
        @Schema(description = "미각 타입")
        FlavorInfo flavor,
        @Schema(description = "미각 정보의 점수와 설명 리스트")
        List<Level> levels
) {
}

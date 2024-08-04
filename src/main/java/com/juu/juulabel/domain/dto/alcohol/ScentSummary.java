package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "향 간단 정보")
public record ScentSummary(
        @Schema(description = "형 고유 번호", example = "1")
        Long id,
        @Schema(description = "향 이름", example = "꽃")
        String name
) {
}

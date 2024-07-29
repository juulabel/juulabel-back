package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "양조장 간단 정보")
public record BrewerySummary(
        @Schema(description = "양조장 고유 번호", example = "38")
        Long id,
        @Schema(description = "양조장 이름", example = "느린마을 양조장")
        String name
) {
}

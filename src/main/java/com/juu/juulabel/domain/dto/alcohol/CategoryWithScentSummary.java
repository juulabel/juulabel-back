package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주종별 향 간단 정보")
public record CategoryWithScentSummary(
        @Schema(description = "카테고리 고유 번호", example = "20")
        Long id,
        @Schema(description = "코드", example = "SC000")
        String code,
        @Schema(description = "카테고리명", example = "자연")
        String name,
        @Schema(description = "향 간단 정보")
        List<ScentSummary> scents
) {
}

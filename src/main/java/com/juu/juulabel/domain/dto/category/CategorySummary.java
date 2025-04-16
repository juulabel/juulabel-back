package com.juu.juulabel.domain.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 간단 정보")
public record CategorySummary(
        @Schema(description = "카테고리 고유 번호", example = "20")
        Long id,
        @Schema(description = "카테고리명", example = "자연")
        String name,
        @Schema(description = "코드", example = "SC000")
        String code
) {
}

package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "맛 정보")
public record FlavorInfo(
        @Schema(description = "맛 고유 번호", example = "1")
        Long id,
        @Schema(description = "맛 이름(한국어)", example = "신맛, 단맛")
        String name
) {
}

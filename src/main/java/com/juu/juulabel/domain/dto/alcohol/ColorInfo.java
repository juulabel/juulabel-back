package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "색상 정보")
public record ColorInfo(
        @Schema(description = "색상 고유 번호", example = "1")
        Long id,
        @Schema(description = "컬러 이름(한국어)", example = "검은색")
        String name,
        @Schema(description = "rgb", example = "#001302")
        String rgb
) {
}

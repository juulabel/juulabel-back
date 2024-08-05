package com.juu.juulabel.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시음노트 작성 응답")
public record TastingNoteWriteResponse(
        @Schema(description = "작성한 시음노트 고유 번호")
        Long id
) {
}

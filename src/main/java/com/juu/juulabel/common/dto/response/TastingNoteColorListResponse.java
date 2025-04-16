package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.request.ColorInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "시각 정보 조회")
public record TastingNoteColorListResponse(
        @Schema(description = "주종에 따른 색상 리스트")
        List<ColorInfo> colors
) {
}

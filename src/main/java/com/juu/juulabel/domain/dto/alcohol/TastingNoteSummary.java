package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "전통주 감각 간단 정보")
public record TastingNoteSummary (
        @Schema(description = "색상")
        List<ColorInfo> colors,
        @Schema(description = "탁도")
        String turbidity,
        @Schema(description = "탄산도")
        String carbonation,
        @Schema(description = "점성도")
        String viscosity,
        @Schema(description = "향")
        String scent
){
}

package com.juu.juulabel.domain.dto.alcohol;

import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "촉각 타입 정보")
public record SensoryTypeInfo(
        @Schema(description = "촉각 타입", example = "CARBONATION")
        SensoryType sensory,
        @Schema(description = "촉각 타입에 대한 설명", example = "탄산도")
        String description
) {
        public static SensoryTypeInfo of(SensoryType sensoryType) {
                return new SensoryTypeInfo(sensoryType, sensoryType.getDescription());
        }
}

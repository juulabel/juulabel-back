package com.juu.juulabel.domain.dto.alcohol;

import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "촉각 타입 정보")
public record FlavorTypeInfo(
        @Schema(description = "미각 타입", example = "SOURNESS")
        FlavorType flavor,
        @Schema(description = "촉각 타입에 대한 설명", example = "감칠맛")
        String description
) {
        public static FlavorTypeInfo of(FlavorType flavorType) {
                return new FlavorTypeInfo(flavorType, flavorType.getDescription());
        }
}

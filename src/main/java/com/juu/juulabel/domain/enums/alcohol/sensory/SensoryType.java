package com.juu.juulabel.domain.enums.alcohol.sensory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "촉각 타입")
@Getter
public enum SensoryType {
    CARBONATION("탄산도"),
    CLARITY("투명도"),
    DENSITY("진하기"),
    SEDIMENT("침전물"),
    TURBIDITY("탁도"),
    VISCOSITY("점성도");

    private final String description;

    SensoryType(String description) {
        this.description = description;
    }

}

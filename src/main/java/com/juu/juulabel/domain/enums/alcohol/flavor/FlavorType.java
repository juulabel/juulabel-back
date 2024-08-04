package com.juu.juulabel.domain.enums.alcohol.flavor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "미각 타입")
@Getter
public enum FlavorType {
    SWEETNESS("단맛"),
    SOURNESS("신맛"),
    BITTERNESS("쓴맛"),
    UMAMI("감칠맛"),
    AFTERTASTE("여운"),
    BODY("무게감");

    private final String description;

    FlavorType(String description) {
        this.description = description;
    }

}

package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

public record FlavorDetail (
        @Schema(description = "맛 이름")
        String name,
        @Schema(description = "맛 점수")
        int score
){
}

package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "양조장과 연결된 전통주 간단 정보")
public record AlcoholicBrewerySummary(
        @Schema(description = "전통주 고유 번호", example = "38")
        Long alcoholicDrinksId,
        @Schema(description = "전통주명", example = "탁100")
        String alcoholicDrinksName,
        @Schema(description = "전통주 타입")
        String alcoholType,
        @Schema(description = "전통주 썸네일 URL")
        String thumbnail
    ){
}

package com.juu.juulabel.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "전통주 검색 요청")
public record CategorySearchAlcoholRequest(
        @Schema(description = "카테고리검색", example = "탁주")
        Long type,
//        @Schema(description = "마지막 전통주 이름", example = "탁100 네추럴")
//        String lastAlcoholicDrinksName,
        @Schema(description = "페이지 사이즈", example = "1")
        @NotNull(message = "페이지 사이즈가 누락되었습니다.")
        int pageSize
) {
}
package com.juu.juulabel.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "유저 추천 리스트 조회 요청")
public record RecommendListRequest(
        @Schema(description = "마지막 벳지 추천 유저 고유 번호", example = "20")
        Long badgeLastUserId,
        @Schema(description = "마지막 취향 추천 유저 고유 번호", example = "20")
        Long tastingLastUserId,
        @Schema(description = "페이지 사이즈", example = "1")
        @NotNull(message = "페이지 사이즈가 누락되었습니다.")
        int pageSize
) {
}

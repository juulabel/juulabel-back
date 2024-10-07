package com.juu.juulabel.domain.dto.tastingnote;

import io.swagger.v3.oas.annotations.media.Schema;

public record LikeTopTastingNoteSummary(
        @Schema(description = "프로필 이미지")
        String profileImage,
        @Schema(description = "글쓴이 닉네임")
        String nickname,
        @Schema(description = "좋아요 갯수")
        long count,
        @Schema(description = "시음노트 내용")
        String content
) {
}

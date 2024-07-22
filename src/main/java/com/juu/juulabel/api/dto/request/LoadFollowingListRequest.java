package com.juu.juulabel.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "팔로잉 리스트 조회 요청")
public record LoadFollowingListRequest(
        @Schema(description = "마지막 팔로우 고유 번호")
        Long lastFollowId,
        @Schema(description = "페이지 사이즈")
        @NotNull(message = "페이지 사이즈가 누락되었습니다.")
        int pageSize
) {
}

package com.juu.juulabel.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "팔로우, 언팔로우 요청")
public record FollowOrUnfollowRequest(
        @Schema(description = "팔로우, 언팔로우 회원 고유 번호", example = "182", nullable = true)
        @NotNull(message = "followee 정보가 누락되었습니다.")
        Long followeeId
) {
}

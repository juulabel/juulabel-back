package com.juu.juulabel.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팔로워 삭제 응답")
public record FollowDeleteResponse(
        @Schema(description = "팔로우 여부")
        boolean isFollowed
) {
}
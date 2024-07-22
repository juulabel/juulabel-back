package com.juu.juulabel.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팔로잉, 언팔로잉 응답")
public record FollowOrUnfollowResponse(
        @Schema(description = "팔로우 여부")
        boolean isFollowed
) {
}

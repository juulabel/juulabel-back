package com.juu.juulabel.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record FollowOrUnfollowRequest(
        @NotNull(message = "followee 정보가 누락되었습니다.")
        Long followeeId
) {
}

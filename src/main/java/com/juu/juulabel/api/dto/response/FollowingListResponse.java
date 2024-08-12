package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.follow.FollowUser;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

@Schema(description = "팔로잉 리스트 조회 응답")
public record FollowingListResponse(
        @Schema(description = "팔로잉 리스트")
        Slice<FollowUser> followings
) {
}

package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.follow.response.FollowUser;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

@Schema(description = "팔로워 리스트 조회 응답")
public record FollowerListResponse(
        @Schema(description = "팔로워 수")
        long count,
        @Schema(description = "팔로워 리스트")
        Slice<FollowUser> followers
) {
}

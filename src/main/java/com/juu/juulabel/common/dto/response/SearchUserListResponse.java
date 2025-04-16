package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.follow.request.FollowUser;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

@Schema( description = "검색 된 유저 리스트 조회 응답" )
public record SearchUserListResponse(
        @Schema(description = "검색 된 유저 리스트")
        Slice<FollowUser> followers
) {
}
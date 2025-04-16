package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.follow.response.FollowUser;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

@Schema(description = "유저 추천 조회 응답")
public record RecommendListResponse(
        @Schema(description = "벳지 유저 추천 리스트")
        Slice<FollowUser> badgeRecommendUser,

        @Schema(description = "취향 유사도 추천 리스트")
        Slice<FollowUser> tastingRecommendUser
) {
}

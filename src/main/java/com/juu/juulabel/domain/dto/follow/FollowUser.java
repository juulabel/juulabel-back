package com.juu.juulabel.domain.dto.follow;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팔로우 유저 정보")
public record FollowUser(
        @Schema(description = "회원 고유 번호", example = "38")
        Long id,
        @Schema(description = "닉네임", example = "장아찌")
        String nickname,
        @Schema(description = "프로필 이미지 링크")
        String profileImage,
        @Schema(description = "로그인 유저가 팔로우하고 있는지 여부")
        boolean isFollowed
) {
}

package com.juu.juulabel.api.dto.response;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberProfileResponse(
    @Schema(description = "회원 고유 번호", example = "38")
    Long id,
    @Schema(description = "회원 닉네임", example = "느린마을 거북이")
    String nickname,
    @Schema(description = "프로필 이미지 URL")
    String profileImage,
    @Schema(description = "회원 자기소개")
    String introduction,
    @Schema(description = "회원이 작성한 시음노트 개수")
    long tastingNoteCount,
    @Schema(description = "회원이 작성한 일상생활 개수")
    long dailyLifeCount,
    @Schema(description = "팔로잉 수")
    long followingCount,
    @Schema(description = "팔로워 수")
    long followerCount,
    @Schema(description = "로그인 유저가 팔로우하고 있는지 여부")
    boolean isFollowed
) {
}

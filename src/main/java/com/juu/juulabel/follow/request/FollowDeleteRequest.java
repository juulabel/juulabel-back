package com.juu.juulabel.follow.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "팔로우 삭제 요청")
public record FollowDeleteRequest(
    @Schema(description = "팔로우 삭제 할 회원 고유 번호" , example = "17" , nullable = true)
    @NotNull(message = "followee 정보가 누락되었습니다")
    Long followerId
){
        }

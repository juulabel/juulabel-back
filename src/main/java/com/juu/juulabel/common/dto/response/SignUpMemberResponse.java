package com.juu.juulabel.common.dto.response;

@Deprecated
public record SignUpMemberResponse(
        Long memberId,
        String accessToken) {
}
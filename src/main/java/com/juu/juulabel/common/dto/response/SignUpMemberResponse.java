package com.juu.juulabel.common.dto.response;

public record SignUpMemberResponse(
        Long memberId,
        String accessToken) {
}
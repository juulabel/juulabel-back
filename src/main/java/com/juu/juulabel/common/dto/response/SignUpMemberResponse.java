package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.member.token.Token;

public record SignUpMemberResponse(
    Long memberId,
    Token token
) {
}
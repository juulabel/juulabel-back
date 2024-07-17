package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.token.Token;

public record SignUpMemberResponse(
    Long memberId,
    Token token
) {
}
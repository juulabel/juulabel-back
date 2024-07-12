package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.token.Token;

public record LoginResponse(
        Token token,
        boolean isNewMember
) {
}

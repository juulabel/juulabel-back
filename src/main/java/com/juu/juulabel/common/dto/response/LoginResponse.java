package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.member.request.OAuthUserInfo;
import com.juu.juulabel.member.token.Token;

public record LoginResponse(
        Token token,
        boolean isNewMember,
        OAuthUserInfo oAuthUserInfo
) {
}

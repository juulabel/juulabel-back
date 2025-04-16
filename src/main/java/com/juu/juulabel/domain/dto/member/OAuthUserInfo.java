package com.juu.juulabel.domain.dto.member;

import com.juu.juulabel.domain.enums.member.Provider;

public record OAuthUserInfo(
    String email,
    String providerId,
    Provider provider
) {
}
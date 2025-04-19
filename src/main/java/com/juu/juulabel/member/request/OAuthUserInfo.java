package com.juu.juulabel.member.request;

import com.juu.juulabel.member.domain.Provider;

public record OAuthUserInfo(
    String email,
    String providerId,
    Provider provider
) {
}
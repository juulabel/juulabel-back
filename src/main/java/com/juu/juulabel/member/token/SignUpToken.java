package com.juu.juulabel.member.token;

import com.juu.juulabel.member.domain.Provider;

public record SignUpToken(
        String token,
        String email,
        Provider provider,
        String providerId,
        String nonce) {
}

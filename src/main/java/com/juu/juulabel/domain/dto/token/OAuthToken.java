package com.juu.juulabel.domain.dto.token;

public interface OAuthToken {
    String tokenType();
    String accessToken();
    int expiresIn();
    String refreshToken();
    String scope();
    int refreshTokenExpiresIn();
}

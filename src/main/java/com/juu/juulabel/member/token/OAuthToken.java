package com.juu.juulabel.member.token;

public interface OAuthToken {
    String idToken();
    String tokenType();
    String accessToken();
    int expiresIn();
    String refreshToken();
    String scope();
    int refreshTokenExpiresIn();
}

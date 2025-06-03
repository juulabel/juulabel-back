package com.juu.juulabel.member.token;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleToken(
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("id_token") String idToken) implements OAuthToken {

    @Override
    public String scope() {
        return null;
    }

    @Override
    public int refreshTokenExpiresIn() {
        return 0;
    }
}

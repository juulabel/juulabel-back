package com.juu.juulabel.domain.dto.token;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleToken(
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") int expiresIn,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("scope") String scope,
    @JsonProperty("id_token") String idToken
) implements OAuthToken {
    @Override
    public int refreshTokenExpiresIn() {
        return 0;
    }
}

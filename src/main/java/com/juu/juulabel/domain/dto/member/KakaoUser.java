package com.juu.juulabel.domain.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUser(
        @JsonProperty("id") Long id,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) implements OAuthUser {
    @Override
    public String email() {
        return kakaoAccount.email();
    }
}

package com.juu.juulabel.member.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juu.juulabel.member.domain.Provider;

public record KakaoUser(
        @JsonProperty("id") String id,
        @JsonProperty("has_signed_up") String hasSignedUp,
        @JsonProperty("connected_at") String connectedAt,
        @JsonProperty("synched_at") String synchedAt,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount) implements OAuthUser {
    @Override
    public String email() {
        return kakaoAccount.email();
    }

    @Override
    public Provider provider() {
        return Provider.KAKAO;
    }

}

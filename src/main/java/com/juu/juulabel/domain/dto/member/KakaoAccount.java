package com.juu.juulabel.domain.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(
        @JsonProperty("email") String email
) {
}
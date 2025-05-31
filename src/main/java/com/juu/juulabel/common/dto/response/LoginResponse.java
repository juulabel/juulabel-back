package com.juu.juulabel.common.dto.response;

public record LoginResponse(
        String accessToken,
        String signUpToken,
        String email) {
}

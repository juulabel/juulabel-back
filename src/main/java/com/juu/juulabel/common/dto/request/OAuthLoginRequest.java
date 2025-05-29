package com.juu.juulabel.common.dto.request;

import com.juu.juulabel.member.domain.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OAuthLoginRequest(
        @NotBlank(message = "인가코드가 누락되었습니다.") String code,
        @NotNull(message = "리다이렉트 URI가 누락되었습니다.") String redirectUri,
        @NotNull(message = "가입 경로가 누락되었습니다.") Provider provider) {

}

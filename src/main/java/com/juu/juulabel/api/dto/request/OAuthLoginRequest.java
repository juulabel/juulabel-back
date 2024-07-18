package com.juu.juulabel.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.domain.dto.member.OAuthLoginInfo;
import com.juu.juulabel.domain.enums.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record OAuthLoginRequest(
    @NotBlank(message = "인가코드가 누락되었습니다.")
    String code,
    @JsonProperty("redirectUri")
    String redirectUri,
    @NotNull(message = "가입 경로가 누락되었습니다.")
    Provider provider
) {
    public OAuthLoginInfo toDto() {
        Map<String, String> propertyMap = Map.of(
            AuthConstants.CODE, code,
            AuthConstants.REDIRECT_URI, redirectUri
        );
        return new OAuthLoginInfo(provider, propertyMap);
    }
}

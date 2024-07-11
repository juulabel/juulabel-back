package com.juu.juulabel.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.domain.dto.member.OAuthLoginInfo;
import com.juu.juulabel.domain.enums.Provider;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record GoogleLoginRequest(
    @NotBlank
    String code,
    @JsonProperty("redirectUri")
    String redirectUri
) {
    public OAuthLoginInfo toDto() {
        Map<String, String> propertyMap = Map.of(
            AuthConstants.CODE, code,
            AuthConstants.REDIRECT_URI, redirectUri
        );
        return new OAuthLoginInfo(Provider.GOOGLE, propertyMap);
    }
}

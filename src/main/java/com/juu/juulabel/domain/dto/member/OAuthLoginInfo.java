package com.juu.juulabel.domain.dto.member;

import com.juu.juulabel.domain.enums.member.Provider;

import java.util.Map;

public record OAuthLoginInfo(
        Provider provider,
        Map<String, String> propertyMap
) {
}

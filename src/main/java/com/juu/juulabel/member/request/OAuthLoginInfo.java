package com.juu.juulabel.member.request;

import com.juu.juulabel.member.domain.Provider;

import java.util.Map;

public record OAuthLoginInfo(
        Provider provider,
        Map<String, String> propertyMap
) {
}

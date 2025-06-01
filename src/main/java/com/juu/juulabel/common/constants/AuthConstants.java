package com.juu.juulabel.common.constants;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_NAME = "REFRESH-TOKEN";

    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(15);
    public static final Duration SIGN_UP_TOKEN_DURATION = Duration.ofMinutes(15);

    public static final Duration SOCIAL_LINK_DURATION = Duration.ofMinutes(20);

    // Redis Prefixt
    public static final String SOCIAL_LINK_PREFIX = "social_link";
    public static final String REFRESH_TOKEN_HASH_PREFIX = "refresh_token";
    public static final String REFRESH_TOKEN_INDEX_PREFIX = "refresh_index";
}

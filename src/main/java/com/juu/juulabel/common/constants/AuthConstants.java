package com.juu.juulabel.common.constants;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_HEADER_NAME = "Refresh-Token";

    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(30);
    public static final Duration SOCIAL_LINK_DURATION = Duration.ofMinutes(30);

    // Redis Prefix
    public static final String SOCIAL_LINK_PREFIX = "social_link";
    public static final String REFRESH_TOKEN_HASH_PREFIX = "refresh_toekn";
    public static final String REFRESH_TOKEN_INDEX_PREFIX = "refresh_index";

}

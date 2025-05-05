package com.juu.juulabel.common.constants;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {

    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirectUri";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(30);
    public static final String REFRESH_TOKEN_HEADER_NAME = "X-Refresh-Token";

}

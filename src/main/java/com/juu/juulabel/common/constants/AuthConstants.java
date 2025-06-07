package com.juu.juulabel.common.constants;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_TOKEN_NAME = "auth_token";
    public static final String SIGN_UP_TOKEN_NAME = "sign_up_token";

    public static final int USER_SESSION_TTL = 60 * 60 * 24 * 7; // 7 days

    // Redis Prefix
    public static final String USER_SESSION_PREFIX = "user_session";
}

package com.juu.juulabel.common.provider.token;

import java.time.Duration;
import java.util.function.Function;

import org.springframework.util.StringUtils;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.exception.InvalidParamException;

public abstract class TokenProvider<T> {

    public static final String ISSUER = "juulabel.com";
    protected final Duration duration;

    protected TokenProvider(Duration duration) {
        this.duration = duration;
    }

    public String resolveToken(String header) {
        if (!StringUtils.hasText(header)) {
            throw new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION);
        }
        return header.replace(AuthConstants.TOKEN_PREFIX, "");
    }

    public abstract <F> F extractFromClaims(String token, Function<T, F> claimsResolver);

    public abstract T parseClaims(String token);
}

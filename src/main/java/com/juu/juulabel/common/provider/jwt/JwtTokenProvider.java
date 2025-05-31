package com.juu.juulabel.common.provider.jwt;

import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.constants.AuthConstants;

import io.jsonwebtoken.*;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
public abstract class JwtTokenProvider {
    protected static final String ISSUER = "juulabel";
    protected static final String ROLE_CLAIM = "role";

    protected final SecretKey key;
    protected final JwtParser jwtParser;

    protected JwtTokenProvider(String secretKey) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        this.jwtParser = Jwts.parser().verifyWith(this.key).build();
    }

    public String resolveToken(String header) {
        if (!StringUtils.hasText(header)) {
            throw new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION);
        }
        return header.replace(AuthConstants.TOKEN_PREFIX, "");
    }

    protected <T> T extractFromClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(parseClaims(token));
    }

    protected Claims parseClaims(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (SignatureException | MalformedJwtException ex) {
            throw new CustomJwtException(ErrorCode.JWT_MALFORMED_EXCEPTION);
        } catch (ExpiredJwtException ex) {
            throw new CustomJwtException(ErrorCode.JWT_EXPIRED_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomJwtException(ErrorCode.JWT_UNSUPPORTED_EXCEPTION);
        } catch (IllegalArgumentException ex) {
            throw new CustomJwtException(ErrorCode.JWT_ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

}

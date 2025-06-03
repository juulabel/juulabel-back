package com.juu.juulabel.common.provider.token.jwt;

import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.provider.token.TokenProvider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Component
public abstract class JwtTokenProvider extends TokenProvider<Claims> {

    protected Key key;
    protected JwtParser jwtParser;

    protected JwtTokenProvider(String secretKey, Duration duration) {
        super(duration);
        this.key = secretKey != null ? Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)) : null;
        this.jwtParser = this.key != null ? Jwts.parser().verifyWith((SecretKey) this.key).build() : null;
    }

    public JwtBuilder build(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.duration.toMillis()))
                // .audience(ISSUER)
                .signWith(key);
    }

    @Override
    public <T> T extractFromClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(parseClaims(token));
    }

    @Override
    public Claims parseClaims(String token) {
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

package com.juu.juulabel.common.provider.token.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.provider.token.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 * JWT-specific implementation of TokenService
 */
public class JwtTokenService implements TokenService<Claims> {
    
    public static final String DEFAULT_ISSUER = "juulabel.com";
    
    private final SecretKey key;
    private final JwtParser jwtParser;
    private final Duration tokenDuration;
    
    public JwtTokenService(String secretKey, Duration duration) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        this.jwtParser = Jwts.parser().verifyWith(this.key).build();
        this.tokenDuration = duration;
    }
    
    @Override
    public String createToken(Claims claims) {
        // For now, we'll focus on the Map-based approach which works better
        throw new UnsupportedOperationException("Use createToken(Map<String, Object>) instead");
    }
    
    /**
     * Creates a token with custom claims map
     */
    public String createToken(Map<String, Object> claimsMap) {
        return Jwts.builder()
                .claims(claimsMap)
                .issuer(DEFAULT_ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenDuration.toMillis()))
                .signWith(key)
                .compact();
    }
    
    /**
     * Creates a JWT builder with claims for more advanced token creation
     */
    public JwtBuilder createBuilder(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuer(DEFAULT_ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenDuration.toMillis()))
                .signWith(key);
    }
    
    @Override
    public Claims parseToken(String token) {
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
    
    @Override
    public <R> R extractFromToken(String token, Function<Claims, R> extractor) {
        return extractor.apply(parseToken(token));
    }
    
    @Override
    public boolean isValidToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 
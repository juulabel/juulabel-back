package com.juu.juulabel.common.provider.token.paseto;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.juu.juulabel.common.exception.CustomPasetoException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.provider.token.TokenService;

import dev.paseto.jpaseto.Claims;
import dev.paseto.jpaseto.ExpiredPasetoException;
import dev.paseto.jpaseto.IncorrectClaimException;
import dev.paseto.jpaseto.MissingClaimException;
import dev.paseto.jpaseto.PasetoException;
import dev.paseto.jpaseto.PasetoIOException;
import dev.paseto.jpaseto.PasetoKeyException;
import dev.paseto.jpaseto.PasetoParser;
import dev.paseto.jpaseto.PasetoSignatureException;
import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.RequiredTypeException;
import dev.paseto.jpaseto.lang.Keys;

/**
 * PASETO-specific implementation of TokenService
 */
public class PasetoTokenService implements TokenService<Claims> {

    public static final String DEFAULT_ISSUER = "juulabel.com";
    public static final String DEFAULT_AUDIENCE = "juu-label-client";

    private final SecretKey secretKey;
    private final PasetoParser parser;
    private final Duration tokenDuration;

    public PasetoTokenService(String secretKey, Duration duration) {
        this.secretKey = Keys.secretKey(secretKey.getBytes());
        this.parser = Pasetos.parserBuilder()
                .setSharedSecret(this.secretKey)
                .build();
        this.tokenDuration = duration;
    }

    @Override
    public String createToken(Claims claims) {
        // For now, we'll focus on the Map-based approach which works
        throw new UnsupportedOperationException("Use createToken(Map<String, Object>) instead");
    }

    /**
     * Creates a token with custom claims map
     */
    public String createToken(Map<String, Object> claimsMap) {
        var builder = Pasetos.V2.LOCAL.builder()
                .setSharedSecret(secretKey)
                .setIssuer(DEFAULT_ISSUER)
                .setIssuedAt(Instant.now())
                .setExpiration(Instant.now().plus(tokenDuration));

        // Add custom claims
        claimsMap.forEach(builder::claim);

        return builder.compact();
    }

    @Override
    public Claims parseToken(String token) {
        try {
            return parser.parse(token).getClaims();
        } catch (ExpiredPasetoException e) {
            throw new CustomPasetoException(ErrorCode.PAS_EXPIRED_EXCEPTION);
        } catch (PasetoSignatureException | PasetoKeyException e) {
            throw new CustomPasetoException(ErrorCode.PAS_SECURITY_EXCEPTION);
        } catch (PasetoIOException e) {
            throw new CustomPasetoException(ErrorCode.PAS_IO_EXCEPTION);
        } catch (MissingClaimException | IncorrectClaimException | RequiredTypeException e) {
            throw new CustomPasetoException(ErrorCode.PAS_ILLEGAL_ARGUMENT_EXCEPTION);
        } catch (PasetoException e) {
            throw new CustomPasetoException(ErrorCode.PAS_UNSUPPORTED_EXCEPTION);
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
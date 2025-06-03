package com.juu.juulabel.common.provider.token.paseto;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.juu.juulabel.common.exception.CustomPasetoException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.provider.token.TokenProvider;

import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.PasetoParser;
import dev.paseto.jpaseto.Claims;
import dev.paseto.jpaseto.PasetoIOException;
import dev.paseto.jpaseto.PasetoException;
import dev.paseto.jpaseto.PasetoKeyException;
import dev.paseto.jpaseto.PasetoSignatureException;
import dev.paseto.jpaseto.PasetoV2LocalBuilder;
import dev.paseto.jpaseto.ExpiredPasetoException;
import dev.paseto.jpaseto.MissingClaimException;
import dev.paseto.jpaseto.IncorrectClaimException;
import dev.paseto.jpaseto.RequiredTypeException;
import dev.paseto.jpaseto.lang.Keys;

public abstract class PasetoTokenProvider extends TokenProvider<Claims> {

    protected final PasetoParser parser;
    protected final SecretKey key;

    protected PasetoTokenProvider(String secretKey, Duration duration) {
        super(duration);
        this.key = Keys.secretKey(secretKey.getBytes());
        this.parser = Pasetos.parserBuilder()
                .setSharedSecret(this.key)
                .build();
    }

    protected PasetoV2LocalBuilder builder() {
        return Pasetos.V2.LOCAL.builder()
                .setSharedSecret(this.key)
                .setIssuer(ISSUER)
                .setAudience("juu-label-client")
                .setIssuedAt(Instant.now())
                .setExpiration(Instant.now().plus(this.duration));
    }

    @Override
    public <T> T extractFromClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(parseClaims(token));
    }

    public Claims parseClaims(String token) {
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
}

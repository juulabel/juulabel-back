package com.juu.juulabel.common.provider;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;

@Component
public class JwtTokenProvider {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofDays(1).toMillis();
    private static final String ISSUER = "juulabel";
    private static final String ROLE_CLAIM = "role";

    private final SecretKey key;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Member member) {
        return Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim(ROLE_CLAIM, member.getRole().name())
                .issuedAt(new Date())
                .issuer(ISSUER)
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }


    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> roles =
                Collections.singletonList(new SimpleGrantedAuthority(claims.get(ROLE_CLAIM, String.class)));

        Member member = Member.builder()
                .id(Long.parseLong(claims.getSubject()))
                .build();

        return new UsernamePasswordAuthenticationToken(
                member,
                null,
                roles
        );
    }

    public String resolveToken(String header) {
        return Optional.ofNullable(header)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION))
                .replace(AuthConstants.TOKEN_PREFIX, "");
    }

    public boolean isValidateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        return !getExpirationByToken(token).before(new Date());
    }

    public Date getExpirationByToken(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException ex) {
            throw new CustomJwtException(ErrorCode.MALFORMED_JWT_EXCEPTION);
        } catch (ExpiredJwtException ex) {
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomJwtException(ErrorCode.UNSUPPORTED_JWT_EXCEPTION);
        } catch (IllegalArgumentException ex) {
            throw new CustomJwtException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

}

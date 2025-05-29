package com.juu.juulabel.common.provider;

import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberRole;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.juu.juulabel.common.constants.AuthConstants.*;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    private static final String ISSUER = "juulabel";
    private static final String ROLE_CLAIM = "role";

    private final SecretKey key;
    private final JwtParser jwtParser;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String key) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
        this.jwtParser = Jwts.parser().verifyWith(this.key).build();
    }

    public String createAccessToken(Member member) {
        return buildToken(member.getId(), member.getRole().name(), ACCESS_TOKEN_DURATION);
    }

    public String createRefreshToken(Member member) {
        return buildToken(member.getId(), member.getRole().name(), REFRESH_TOKEN_DURATION);
    }

    private String buildToken(Long memberId, String role, Duration duration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + duration.toMillis());

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(now)
                .issuer(ISSUER)
                .expiration(expirationDate)
                .signWith(key);

        if (role != null) {
            builder.claim(ROLE_CLAIM, role);
        }

        return builder.compact();
    }

    public Authentication getAuthentication(String accessToken) {
        return extractFromClaims(accessToken, claims -> {
            String role = claims.get(ROLE_CLAIM, String.class);
            Long memberId = Long.parseLong(claims.getSubject());

            Member member = Member.builder()
                    .id(memberId)
                    .role(MemberRole.valueOf(role))
                    .build();

            return new UsernamePasswordAuthenticationToken(
                    member,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(role)));
        });
    }

    public Member getMemberFromToken(String token) {
        return extractFromClaims(token, claims -> {
            Long memberId = Long.parseLong(claims.getSubject());
            String role = claims.get(ROLE_CLAIM, String.class);

            return Member.builder()
                    .id(memberId)
                    .role(role != null ? MemberRole.valueOf(role) : MemberRole.ROLE_USER)
                    .build();
        });
    }

    public String resolveToken(String header) {
        if (!StringUtils.hasText(header)) {
            throw new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION);
        }
        return header.replace(TOKEN_PREFIX, "");
    }

    public boolean isValidateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        try {
            return !parseClaims(token).getExpiration().before(new Date());
        } catch (CustomJwtException e) {
            return false;
        }
    }

    public Date getExpirationByToken(String token) {
        return extractFromClaims(token, Claims::getExpiration);
    }

    private <T> T extractFromClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(parseClaims(token));
    }

    private Claims parseClaims(String token) {
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

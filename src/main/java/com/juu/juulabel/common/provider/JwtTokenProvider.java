package com.juu.juulabel.common.provider;

import com.juu.juulabel.auth.domain.ClientId;
import com.juu.juulabel.auth.domain.RefreshToken;
import com.juu.juulabel.auth.repository.redis.RefreshTokenRedisRepository;
import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.util.HttpRequestUtil;
import com.juu.juulabel.common.util.HttpResponseUtil;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.juu.juulabel.common.constants.AuthConstants.ACCESS_TOKEN_DURATION;
import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_DURATION;
import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_HEADER_NAME;
import static com.juu.juulabel.common.constants.AuthConstants.TOKEN_PREFIX;

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
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String key,
            RefreshTokenRedisRepository refreshTokenRedisRepository) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
        this.jwtParser = Jwts.parser().verifyWith(this.key).build();
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    }

    /**
     * Creates an access token for a member
     * 
     * @param member The member for whom to create the token
     * @return The JWT access token string
     */
    public String createAccessToken(Member member) {
        return buildToken(member.getId(), member.getRole().name(), ACCESS_TOKEN_DURATION);
    }

    /**
     * Creates a refresh token entity for a member
     * 
     * @param member The member for whom to create the token
     * @return A RefreshToken entity
     */
    public RefreshToken createRefreshToken(Long memberId, String parentTokenId) {
        String ipAddress = HttpRequestUtil.getClientIpAddress();
        String userAgent = HttpRequestUtil.getUserAgent();
        String deviceId = HttpRequestUtil.getDeviceId();

        String token = buildToken(memberId, null, REFRESH_TOKEN_DURATION);

        HttpResponseUtil.addCookie(REFRESH_TOKEN_HEADER_NAME, token,
                (int) REFRESH_TOKEN_DURATION.getSeconds());

        return RefreshToken.builder()
                .token(token)
                .memberId(memberId)
                .clientId(ClientId.WEB)
                .deviceId(deviceId)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
    }

    /**
     * Builds a JWT token
     * 
     * @param memberId The member ID
     * @param role     The role (can be null)
     * @param duration The duration
     * @return The JWT token string
     */
    private String buildToken(Long memberId, String role, Duration duration) {
        Date expirationDate = new Date(System.currentTimeMillis() + duration.toMillis());
        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(new Date())
                .issuer(ISSUER)
                .expiration(expirationDate)
                .signWith(key);

        if (role != null) {
            builder.claim(ROLE_CLAIM, role);
        }

        return builder.compact();
    }

    /**
     * Gets authentication from an access token
     * 
     * @param accessToken The access token
     * @return The Authentication object
     */
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

    /**
     * Extracts member information from a token
     * 
     * @param token The JWT token
     * @return The Member object
     */
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

    /**
     * Resolves a token from the Authorization header
     * 
     * @param header The Authorization header
     * @return The token without the prefix
     */
    public String resolveToken(String header) {
        if (header == null) {
            throw new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION);
        }
        return header.replace(TOKEN_PREFIX, "");
    }

    /**
     * Validates a token
     * 
     * @param token The JWT token
     * @return true if the token is valid, false otherwise
     */
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

    /**
     * Gets the expiration date from a token
     * 
     * @param token The JWT token
     * @return The expiration date
     */
    public Date getExpirationByToken(String token) {
        return extractFromClaims(token, Claims::getExpiration);
    }

    /**
     * Extracts data from claims using a function
     * 
     * @param token          The JWT token
     * @param claimsResolver The function to extract data from claims
     * @return The extracted data
     */
    private <T> T extractFromClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses claims from a token
     * 
     * @param token The JWT token
     * @return The claims
     * @throws CustomJwtException if the token is invalid
     */
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

    /**
     * Validates a refresh token
     * 
     * @param token     The refresh token
     * @param ipAddress Current request IP address
     * @param userAgent Current request user agent
     * @return true if valid, throws exception otherwise
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rotateRefreshToken(RefreshToken token) {
        String ipAddress = HttpRequestUtil.getClientIpAddress();
        String userAgent = HttpRequestUtil.getUserAgent();
        String deviceId = HttpRequestUtil.getDeviceId();

        Long memberId = token.getMemberId();

        // Case 1: Device ID doesn’t match the previous token
        // • Revoke the entire chain of the previous token (current + descendants).
        // • This blocks access from the stolen session while allowing the user to
        // continue on the new device.
        // • Keep the new device’s session active if validated correctly (e.g., fresh
        // login or MFA).

        if (!token.getDeviceId().equals(deviceId)) {
            refreshTokenRedisRepository.revokeByDeviceId(memberId, deviceId);
            throw new CustomJwtException(
                    String.format(
                            "Device ID mismatch: Device ID=%s, Current Token Device ID=%s",
                            deviceId, token.getDeviceId()),
                    ErrorCode.REFRESH_TOKEN_INVALID);
        }

        // Case 2: Token is reused/revoked
        // • Revoke the entire token from the member.
        // • This blocks access from the stolen session while allowing the user to
        // continue on the new device.
        // • Keep the new device’s session active if validated correctly (e.g., fresh
        // login or MFA).

        if (token.getRevokedAt() != null) {
            refreshTokenRedisRepository.revokeByMemberId(memberId);
            throw new CustomJwtException(
                    String.format(
                            "Parent token is revoked: Device ID=%s IP=%s User-Agent=%s, Parent Token Device ID=%s IP=%s User-Agent=%s",
                            deviceId, ipAddress, userAgent, token.getDeviceId(), token.getIpAddress(),
                            token.getUserAgent()),
                    ErrorCode.REFRESH_TOKEN_INVALID);
        }

    }

    /**
     * Checks for token reuse
     * 
     * @param token          The refresh token
     * @param hasChildTokens Whether the token has child tokens
     * @param ipAddress      Current request IP
     * @param userAgent      Current request user agent
     * @throws CustomJwtException when token reuse is detected
     */
    public void checkTokenReuse(RefreshToken token, boolean hasChildTokens, String ipAddress, String userAgent) {
        if (token.getRevokedAt() != null && hasChildTokens) {
            throw new CustomJwtException(
                    String.format("Refresh token reuse detected: IP=%s User-Agent=%s",
                            ipAddress, userAgent),
                    ErrorCode.REFRESH_TOKEN_INVALID);
        }
    }

}

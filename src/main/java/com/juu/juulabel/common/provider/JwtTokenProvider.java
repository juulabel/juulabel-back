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

import static com.juu.juulabel.common.constants.AuthConstants.ACCESS_TOKEN_DURATION;
import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_DURATION;
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

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String key) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
        this.jwtParser = Jwts.parser().verifyWith(this.key).build();
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
    public String createRefreshToken(Long memberId) {
        return buildToken(memberId, null, REFRESH_TOKEN_DURATION);
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
        Date expirationDate = getExpirationDate(duration);
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
     * Gets an expiration date based on current time plus duration
     * 
     * @param duration The duration
     * @return The expiration date
     */
    private Date getExpirationDate(Duration duration) {
        return new Date(System.currentTimeMillis() + duration.toMillis());
    }

}

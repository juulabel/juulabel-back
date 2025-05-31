package com.juu.juulabel.common.provider.jwt;

import java.time.Duration;
import java.util.Date;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberRole;

import io.jsonwebtoken.Jwts;

public abstract class MemberTokenProvider extends JwtTokenProvider {

    protected MemberTokenProvider(String secretKey) {
        super(secretKey);
    }

    public String createToken(Member member, Duration duration) {
        return Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim(ROLE_CLAIM, member.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duration.toMillis()))
                .signWith(key)
                .compact();
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

}

package com.juu.juulabel.common.provider.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberRole;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class AccessTokenProvider extends MemberTokenProvider {

    public AccessTokenProvider(@Value("${spring.jwt.access-key}") String secretKey) {
        super(secretKey);
    }

    public String createToken(Member member) {
        return this.createToken(member, AuthConstants.ACCESS_TOKEN_DURATION);
    }

    @Override
    public String createToken(Member member, Duration duration) {
        return super.createToken(member, duration);
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
}

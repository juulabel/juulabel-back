package com.juu.juulabel.common.session;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.token.UserSession;

/**
 * Service for creating Spring Security Authentication objects from sessions
 */
@Component
public class SessionAuthenticationProvider {

    /**
     * Creates Spring Security Authentication from UserSession
     * @param session The user session
     * @return Authentication object
     */
    public Authentication createAuthentication(UserSession session) {
        Member member = Member.builder()
                .id(session.getMemberId())
                .role(session.getRole())
                .email(session.getEmail())
                .build();

        return new UsernamePasswordAuthenticationToken(
                member,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(session.getRole().name())));
    }
} 
package com.juu.juulabel.api.principal;

import com.juu.juulabel.common.exception.AuthenticationException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberQueryRepository.getByEmail(username);

        JuulabelMember juulabelMember = new JuulabelMember(member);
        validateAuthenticate(juulabelMember);

        return juulabelMember;
    }

    private void validateAuthenticate(JuulabelMember member) {
        if (member == null) {
            throw new AuthenticationException(ErrorCode.INTERNAL_AUTHENTICATION_SERVICE);
        }
        validateEnabled(member);
        validateAccountExpired(member);
        validateAccountNonLocked(member);
        validateCredentialNonExpired(member);
    }

    private static void validateEnabled(JuulabelMember member) {
        if(!member.isEnabled()){
            throw new AuthenticationException(ErrorCode.DISABLE_ACCOUNT);
        }
    }

    private static void validateCredentialNonExpired(JuulabelMember member) {
        if (!member.isCredentialsNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountNonLocked(JuulabelMember member) {
        if (!member.isAccountNonLocked()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountExpired(JuulabelMember member) {
        if (!member.isAccountNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

}

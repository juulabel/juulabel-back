package com.juu.juulabel.common.provider.token.validator;

import org.springframework.stereotype.Component;

import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.repository.MemberReader;

import lombok.RequiredArgsConstructor;

/**
 * Validator for signup token claims and associated member data
 */
@Component
@RequiredArgsConstructor
public class SignupTokenValidator implements TokenValidator<SignupTokenClaims> {
    
    private static final String EXPECTED_AUDIENCE = "user-signup-completion";
    
    private final MemberReader memberReader;
    
    @Override
    public void validate(SignupTokenClaims claims) {
        validateAudience(claims.audience());
        Member member = memberReader.getByEmail(claims.email());
        validateMemberAgainstClaims(member, claims);
    }
    
    @Override
    public String getValidationType() {
        return "SIGNUP_TOKEN";
    }
    
    private void validateAudience(String audience) {
        if (!EXPECTED_AUDIENCE.equals(audience)) {
            throw new AuthException("Invalid token audience", ErrorCode.INVALID_AUTHENTICATION);
        }
    }
    
    private void validateMemberAgainstClaims(Member member, SignupTokenClaims claims) {
        validateProvider(member, claims);
        validateProviderId(member, claims);
        validateNonce(member, claims);
        validateMemberStatus(member);
    }
    
    private void validateProvider(Member member, SignupTokenClaims claims) {
        if (member.getProvider() != claims.provider()) {
            throw new AuthException("Provider mismatch", ErrorCode.PROVIDER_ID_MISMATCH);
        }
    }
    
    private void validateProviderId(Member member, SignupTokenClaims claims) {
        if (!member.getProviderId().equals(claims.providerId())) {
            throw new AuthException("Provider ID mismatch", ErrorCode.PROVIDER_ID_MISMATCH);
        }
    }
    
    private void validateNonce(Member member, SignupTokenClaims claims) {
        if (!member.getNickname().equals(claims.nonce())) {
            throw new AuthException("Token validation failed", ErrorCode.INVALID_AUTHENTICATION);
        }
    }
    
    private void validateMemberStatus(Member member) {
        if (member.getStatus() != MemberStatus.PENDING) {
            throw new AuthException("Member already completed signup", ErrorCode.INVALID_AUTHENTICATION);
        }
    }
} 
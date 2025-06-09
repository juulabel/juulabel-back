package com.juu.juulabel.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.factory.OAuthProviderFactory;
import com.juu.juulabel.common.properties.RedirectProperties;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.request.OAuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service dedicated to OAuth authentication flow.
 * Handles OAuth provider interactions and user data retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final OAuthProviderFactory providerFactory;
    private final RedirectProperties redirectProperties;
    private final MemberReader memberReader;

    /**
     * Performs OAuth authentication and returns user info
     * 
     * @param provider OAuth provider
     * @param code     Authorization code
     * @return OAuth user information
     */
    public OAuthUser authenticateWithProvider(Provider provider, String code) {
        try {
            String redirectUrl = redirectProperties.getRedirectUrl(provider);
            return providerFactory.getOAuthUser(provider, code, redirectUrl);

        } catch (Exception e) {
            log.error("OAuth authentication failed", e);
            throw new AuthException(ErrorCode.OAUTH_AUTHENTICATION_FAILED);
        }
    }

    /**
     * Determines the member status for OAuth user
     * 
     * @param oAuthUser OAuth user information
     * @return Member status result
     */
    public MemberStatusResult determineMemberStatus(OAuthUser oAuthUser) {
        Optional<Member> memberOpt = memberReader.getOptionalByEmail(oAuthUser.email());

        if (memberOpt.isEmpty()) {
            return new MemberStatusResult(null, null, oAuthUser, true);
        }

        Member member = memberOpt.get();
        validateMemberForLogin(member, oAuthUser);

        return new MemberStatusResult(member.getStatus(), member, oAuthUser, false);
    }

    /**
     * Validates that the member can login with the OAuth user
     */
    private void validateMemberForLogin(Member member, OAuthUser oAuthUser) {
        if (member.getStatus() == MemberStatus.WITHDRAWAL) {
            throw new AuthException(ErrorCode.MEMBER_WITHDRAWN);
        }

        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new AuthException(ErrorCode.MEMBER_NOT_ACTIVE);
        }

        // Validate OAuth user matches member
        validateLoginMember(member, oAuthUser);
    }

    public void validateLoginMember(Member member, OAuthUser oAuthUser) {
        if (member.getDeletedAt() != null) {
            throw new AuthException(ErrorCode.MEMBER_WITHDRAWN);
        }

        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new AuthException(ErrorCode.MEMBER_NOT_ACTIVE);
        }

        if (!member.getProvider().equals(oAuthUser.provider())) {
            throw new AuthException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }
        if (!member.getProviderId().equals(oAuthUser.id())) {
            throw new AuthException(ErrorCode.PROVIDER_ID_MISMATCH);
        }
    }

    /**
     * Result object containing member status and related data
     */
    public record MemberStatusResult(
            MemberStatus status,
            Member member,
            OAuthUser oAuthUser,
            boolean isNewMember) {

        public boolean isPendingMember() {
            return status == MemberStatus.PENDING;
        }

        public boolean isActiveMember() {
            return status == MemberStatus.ACTIVE;
        }
    }
}
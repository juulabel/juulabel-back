package com.juu.juulabel.auth.service;

import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.http.HttpResponseService;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.redis.UserSessionManager;
import com.juu.juulabel.auth.service.OAuthLoginService.MemberStatusResult;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.juu.juulabel.member.request.OAuthUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Refactored authentication service using specialized service components.
 * Acts as an orchestration layer delegating to focused services.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthLoginService oAuthLoginService;
    private final MemberCreationService memberCreationService;
    private final AccountLifecycleService accountLifecycleService;
    private final SignupTokenService signupTokenService;
    private final UserSessionManager sessionManager;
    private final HttpResponseService httpResponseService;

    /**
     * Handles OAuth login flow for both new and existing members.
     * 
     * @param provider OAuth provider (Google, Kakao, Apple)
     * @param code     Authorization code from OAuth provider
     * @param state    State parameter from OAuth provider
     */
    @Transactional
    public void login(Provider provider, String code, String state) {
        try {
            // Authenticate with OAuth provider
            OAuthUser oAuthUser = oAuthLoginService.authenticateWithProvider(provider, code);

            // Determine member status and handle accordingly
            MemberStatusResult memberResult = oAuthLoginService.determineMemberStatus(oAuthUser);

            if (memberResult.isNewMember()) {
                handleNewMember(memberResult.oAuthUser());
            } else if (memberResult.isPendingMember()) {
                handlePendingMember(memberResult.member(), memberResult.oAuthUser());
            } else if (memberResult.isActiveMember()) {
                handleExistingMember(memberResult.member());
            }

        } catch (Exception e) {
            log.error("Login failed for provider {}: {}", provider, e.getMessage());
            Sentry.captureException(e);
            httpResponseService.redirectToError();
        }
    }

    /**
     * Completes member registration with additional information.
     * 
     * @param member        Pre-authenticated member from signup token
     * @param signUpRequest Additional member registration details
     */
    @Transactional
    public void signUp(Member member, SignUpMemberRequest signUpRequest) {
        Member completedMember = memberCreationService.completeSignup(member, signUpRequest);
        sessionManager.createSession(completedMember);

        log.debug("Signup completed successfully for: {}", completedMember.getEmail());
    }

    /**
     * Logs out current user by invalidating their session.
     */
    public void logout() {
        accountLifecycleService.logout();
    }

    /**
     * Permanently deletes member account and creates audit record.
     * 
     * @param member  Authenticated member requesting deletion
     * @param request Withdrawal request with reason
     */
    @Transactional
    public void deleteAccount(Member member, WithdrawalRequest request) {
        accountLifecycleService.deleteAccount(member, request);
    }

    // Private helper methods for different member handling scenarios

    private void handleNewMember(OAuthUser oAuthUser) {
        String nonce = memberCreationService.createPendingMember(oAuthUser);
        signupTokenService.createAndSetToken(oAuthUser, nonce);
        httpResponseService.redirectToSignup(oAuthUser.email());

        log.debug("New member flow initiated for: {}", oAuthUser.email());
    }

    private void handlePendingMember(Member member, OAuthUser oAuthUser) {
        String nonce = memberCreationService.getExistingNonce(member);
        signupTokenService.createAndSetToken(oAuthUser, nonce);
        httpResponseService.redirectToSignup(oAuthUser.email());

        log.debug("Pending member flow initiated for: {}", oAuthUser.email());
    }

    private void handleExistingMember(Member member) {
        sessionManager.createSession(member);
        httpResponseService.redirectToLogin();

        log.debug("Existing member login successful for: {}", member.getEmail());
    }
}
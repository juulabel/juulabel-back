package com.juu.juulabel.auth.service;

import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.factory.OAuthProviderFactory;
import com.juu.juulabel.common.properties.RedirectProperties;
import com.juu.juulabel.common.provider.token.paseto.SignupTokenProvider;
import com.juu.juulabel.common.util.HttpResponseUtil;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.domain.WithdrawalRecord;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.repository.MemberWriter;
import com.juu.juulabel.member.repository.WithdrawalRecordWriter;
import com.juu.juulabel.member.util.MemberUtils;
import com.juu.juulabel.redis.SessionManager;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling authentication operations including login, signup,
 * logout, and account deletion.
 * Provides secure OAuth-based authentication with session management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    private final WithdrawalRecordWriter withdrawalRecordWriter;
    private final MemberUtils memberUtils;
    private final OAuthProviderFactory providerFactory;
    private final SessionManager sessionManager;
    private final RedirectProperties redirectProperties;
    private final SignupTokenProvider signupTokenProvider;
    private final HttpResponseUtil httpResponseUtil;

    /**
     * Handles OAuth login flow for both new and existing members.
     * 
     * @param provider OAuth provider (Google, GitHub, etc.)
     * @param code     Authorization code from OAuth provider
     * @param state    State parameter from OAuth provider
     */
    @Transactional
    public void login(Provider provider, String code, String state) {
        try {

            // Get OAuth user info
            OAuthUser oAuthUser = getOAuthUser(provider, code);

            // Process member based on existence and status
            Optional<Member> memberOpt = memberReader.getOptionalByEmail(oAuthUser.email());

            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                if (member.getStatus() == MemberStatus.PENDING) {
                    handlePendingMember(member, oAuthUser);
                } else {
                    handleExistingMember(member, oAuthUser);
                }
            } else {
                handleNewMember(oAuthUser);
            }

        } catch (Exception e) {
            Sentry.captureException(e);
            httpResponseUtil.redirectToError();
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
        // Validate member status
        if (member.getStatus() != MemberStatus.PENDING) {
            throw new AuthException("Member is not in pending status", ErrorCode.INVALID_AUTHENTICATION);
        }

        // Complete signup process
        member.completeSignUp(signUpRequest);
        memberWriter.store(member);

        // Process additional member data
        memberUtils.processMemberData(member, signUpRequest);

        // Create session for the newly registered member
        sessionManager.createSession(member);
    }

    /**
     * Logs out current user by invalidating their session.
     */
    public void logout() {
        try {
            sessionManager.invalidateSession();
        } catch (Exception e) {
            log.warn("Error during logout: {}", e.getMessage());
            // Don't throw exception for logout failures
        }
    }

    /**
     * Permanently deletes member account and creates audit record.
     * 
     * @param loginMember Authenticated member requesting deletion
     * @param request     Withdrawal request with reason
     */
    @Transactional
    public void deleteAccount(Member loginMember, WithdrawalRequest request) {
        // Validate member can be deleted
        if (loginMember.getStatus() == MemberStatus.WITHDRAWAL) {
            throw new AuthException("Member already withdrawn", ErrorCode.MEMBER_WITHDRAWN);
        }

        // Mark member as deleted (soft delete)
        loginMember.deleteAccount();

        // Create audit record
        WithdrawalRecord withdrawalRecord = WithdrawalRecord.create(
                request.withdrawalReason(),
                loginMember.getEmail(),
                loginMember.getNickname());
        withdrawalRecordWriter.store(withdrawalRecord);

        // Revoke all sessions
        sessionManager.invalidateAllUserSessions(loginMember.getId());
    }

    // Private helper methods

    private OAuthUser getOAuthUser(Provider provider, String code) {
        String redirectUrl = redirectProperties.getRedirectUrl(provider);
        return providerFactory.getOAuthUser(provider, code, redirectUrl);
    }

    private void handleExistingMember(Member member, OAuthUser oAuthUser) {
        // Validate member status
        if (member.getStatus() == MemberStatus.WITHDRAWAL) {
            throw new AuthException("Member has been withdrawn", ErrorCode.MEMBER_WITHDRAWN);
        }

        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new AuthException("Member is not active", ErrorCode.MEMBER_NOT_ACTIVE);
        }

        // Validate OAuth user matches member
        member.validateLoginMember(oAuthUser);

        // Create session and redirect
        sessionManager.createSession(member);
        httpResponseUtil.redirectToLogin();
    }

    private void handlePendingMember(Member member, OAuthUser oAuthUser) {
        // Validate OAuth user matches pending member
        member.validateLoginMember(oAuthUser);

        // Generate new signup token for existing pending member
        String nonce = member.getNickname(); // Use existing nonce
        signupTokenProvider.createToken(oAuthUser, nonce);

        httpResponseUtil.redirectToSignup();
    }

    private void handleNewMember(OAuthUser oAuthUser) {
        // Generate unique nonce for new member
        String nonce = UUID.randomUUID().toString();

        // Create signup token
        signupTokenProvider.createToken(oAuthUser, nonce);

        // Create new pending member
        Member newMember = Member.create(oAuthUser, nonce);
        memberWriter.store(newMember);

        httpResponseUtil.redirectToSignup();
    }
}
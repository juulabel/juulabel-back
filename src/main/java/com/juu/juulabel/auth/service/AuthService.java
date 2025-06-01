package com.juu.juulabel.auth.service;

import com.juu.juulabel.auth.domain.SignUpToken;
import com.juu.juulabel.common.dto.request.OAuthLoginRequest;
import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.dto.response.LoginResponse;
import com.juu.juulabel.common.dto.response.RefreshResponse;
import com.juu.juulabel.common.dto.response.SignUpMemberResponse;
import com.juu.juulabel.common.factory.OAuthProviderFactory;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.WithdrawalRecord;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.repository.MemberWriter;
import com.juu.juulabel.member.repository.WithdrawalRecordWriter;
import com.juu.juulabel.member.util.MemberUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.juu.juulabel.member.request.OAuthUser;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling authentication operations including login, signup,
 * refresh, logout, and account deletion.
 * Provides secure OAuth-based authentication with token management.
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
    private final TokenService tokenService;
    private final SocialLinkService socialLinkService;

    /**
     * Handles OAuth login for both new and existing members.
     * For new members, creates a signup token; for existing members, creates an
     * access token.
     *
     * @param request OAuth login request containing provider and authorization code
     * @return LoginResponse with access token (existing user) or signup token (new
     *         user)
     */
    @Transactional
    public LoginResponse login(OAuthLoginRequest request) {

        final OAuthUser oAuthUser = providerFactory.getOAuthUser(request);
        final Optional<Member> memberOpt = memberReader.getOptionalByEmail(oAuthUser.email());

        return memberOpt
                .map(member -> createExistingMemberResponse(member, oAuthUser))
                .orElseGet(() -> createNewMemberResponse(oAuthUser));
    }

    /**
     * Creates login response for existing members.
     */
    private LoginResponse createExistingMemberResponse(Member member, OAuthUser oAuthUser) {
        member.validateLoginMember(oAuthUser);
        final String accessToken = tokenService.login(member);
        return new LoginResponse(accessToken, null, oAuthUser.email());
    }

    /**
     * Creates login response for new members (signup flow).
     */
    private LoginResponse createNewMemberResponse(OAuthUser oAuthUser) {
        final String nonce = UUID.randomUUID().toString();
        socialLinkService.save(oAuthUser, nonce);
        final String signUpToken = tokenService.createSignUpReadyToken(oAuthUser, nonce);
        return new LoginResponse(null, signUpToken, oAuthUser.email());
    }

    /**
     * Completes member registration using a validated signup token.
     * Creates the member, processes additional data, and generates authentication
     * tokens.
     *
     * @param signUpToken   validated signup token containing OAuth user information
     * @param signUpRequest member registration details
     * @return SignUpMemberResponse with the new member's ID
     */
    @Transactional
    public SignUpMemberResponse signUp(SignUpToken signUpToken, SignUpMemberRequest signUpRequest) {

        final Member member = Member.create(signUpRequest, signUpToken);
        memberWriter.store(member);

        // Process additional member data (alcohol types, terms agreements) if provided
        memberUtils.processMemberData(member, signUpRequest);

        // Generate authentication tokens for the new member
        String accessToken = tokenService.signUp(member);

        return new SignUpMemberResponse(member.getId(), accessToken);
    }

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param refreshToken the current refresh token
     * @return RefreshResponse with the new access token
     */
    @Transactional(readOnly = true)
    public RefreshResponse refresh(String refreshToken) {
        final String accessToken = tokenService.rotate(refreshToken);
        return new RefreshResponse(accessToken);
    }

    /**
     * Logs out a member by revoking their tokens.
     *
     * @param memberId the ID of the member to log out
     */
    @Transactional
    public void logout(Long memberId) {
        tokenService.logout(memberId);
    }

    /**
     * Permanently deletes a member account and creates a withdrawal record.
     * This operation revokes all tokens and marks the member as deleted.
     *
     * @param loginMember the authenticated member requesting account deletion
     * @param request     withdrawal request containing the reason
     */
    @Transactional
    public void deleteAccount(Member loginMember, WithdrawalRequest request) {

        // Mark member as deleted (soft delete)
        loginMember.deleteAccount();

        // Create audit record for withdrawal
        final WithdrawalRecord withdrawalRecord = WithdrawalRecord.create(
                request.withdrawalReason(),
                loginMember.getEmail(),
                loginMember.getNickname());
        withdrawalRecordWriter.store(withdrawalRecord);

        // Revoke all authentication tokens
        tokenService.withdraw(loginMember.getId());
    }
}
package com.juu.juulabel.auth.service;

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
import com.juu.juulabel.member.token.Token;
import com.juu.juulabel.member.util.MemberUtils;
import lombok.RequiredArgsConstructor;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.request.OAuthUserInfo;
import com.juu.juulabel.common.dto.request.OAuthLoginRequest;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public LoginResponse login(OAuthLoginRequest oAuthLoginRequest) {
        // Extract OAuth information
        final OAuthUser oAuthUser = providerFactory.getOAuthUser(oAuthLoginRequest);

        final Provider provider = oAuthLoginRequest.provider();
        final String providerId = oAuthUser.id();
        final String email = oAuthUser.email();

        // Check if member exists
        final Optional<Member> memberOpt = memberReader.getOptionalByEmail(email);
        final boolean isNewMember = memberOpt.isEmpty();

        if (isNewMember) {
            socialLinkService.save(email, provider, providerId);
        } else {
            // For existing members, validate and create tokens
            final Member member = memberOpt.get();
            member.validateLoginMember(provider, providerId);

            // Create refresh token for login (handles device management)
            tokenService.createLoginRefreshToken(member);
        }

        Token accessToken = tokenService.createAccessToken(memberOpt)
                .orElse(new Token(null, null));

        Long memberId = memberOpt.map(Member::getId).orElse(null);

        return new LoginResponse(
                accessToken,
                isNewMember,
                new OAuthUserInfo(
                        memberId,
                        email,
                        providerId,
                        provider));
    }

    @Transactional
    public SignUpMemberResponse signUp(SignUpMemberRequest signUpRequest) {
        socialLinkService.verify(signUpRequest.email(), signUpRequest.provider(), signUpRequest.providerId());

        final Member member = Member.create(signUpRequest);
        memberWriter.store(member);

        memberUtils.processMemberData(member, signUpRequest);

        // Create token pair for new member
        final Token token = tokenService.createTokenPair(member);

        return new SignUpMemberResponse(member.getId(), token);
    }

    public RefreshResponse refresh(String oldToken) {
        final Token newToken = tokenService.rotateRefreshToken(oldToken);
        return new RefreshResponse(newToken.accessToken());
    }

    public void logout(String oldToken) {
        tokenService.revokeRefreshToken(oldToken);
    }

    @Transactional
    public void deleteAccount(Member loginMember, WithdrawalRequest request, String oldToken) {
        // Mark member as deleted
        loginMember.deleteAccount();

        // Create withdrawal record
        final WithdrawalRecord withdrawalRecord = WithdrawalRecord.create(
                request.withdrawalReason(),
                loginMember.getEmail(),
                loginMember.getNickname());
        withdrawalRecordWriter.store(withdrawalRecord);

        // Revoke all tokens
        tokenService.revokeAllRefreshTokens(oldToken);
    }
}
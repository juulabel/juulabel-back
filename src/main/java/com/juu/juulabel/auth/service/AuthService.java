package com.juu.juulabel.auth.service;

import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.dto.response.LoginResponse;
import com.juu.juulabel.common.dto.response.RefreshResponse;
import com.juu.juulabel.common.dto.response.SignUpMemberResponse;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.factory.OAuthProviderFactory;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.WithdrawalRecord;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.repository.MemberWriter;
import com.juu.juulabel.member.repository.WithdrawalRecordReader;
import com.juu.juulabel.member.repository.WithdrawalRecordWriter;
import com.juu.juulabel.member.request.OAuthLoginInfo;
import com.juu.juulabel.member.token.Token;
import com.juu.juulabel.member.util.MemberUtils;
import lombok.RequiredArgsConstructor;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.common.constants.AuthConstants;
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
    private final WithdrawalRecordReader withdrawalRecordReader;
    private final TokenService tokenService;

    @Transactional
    public LoginResponse login(OAuthLoginRequest oAuthLoginRequest) {
        OAuthLoginInfo authLoginInfo = oAuthLoginRequest.toDto();
        Provider provider = authLoginInfo.provider();

        String accessToken = providerFactory.getAccessToken(
                provider,
                authLoginInfo.propertyMap().get(AuthConstants.REDIRECT_URI),
                authLoginInfo.propertyMap().get(AuthConstants.CODE));

        OAuthUser oAuthUser = providerFactory.getOAuthUser(provider, accessToken);
        String email = oAuthUser.email();

        validateNotWithdrawnMember(email);

        boolean isNewMember = !memberReader.existsByEmailAndProvider(email, provider);
        Optional<Member> memberOpt = isNewMember ? Optional.empty() : Optional.of(memberReader.getByEmail(email));

        Optional<Token> token = tokenService.createAccessToken(memberOpt);

        // Create refresh token for existing members
        memberOpt.ifPresent(member -> tokenService.createLoginRefreshToken(member));

        return new LoginResponse(
                token.orElse(new Token(null, null)),
                isNewMember,
                new OAuthUserInfo(
                        memberOpt.map(Member::getId).orElse(null),
                        email,
                        oAuthUser.id(),
                        provider));
    }

    @Transactional
    public SignUpMemberResponse signUp(SignUpMemberRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);

        Member member = Member.create(signUpRequest);
        memberWriter.store(member);

        memberUtils.processAlcoholTypes(member, signUpRequest);
        memberUtils.processTermsAgreements(member, signUpRequest);

        Token token = tokenService.createTokenPair(member);

        return new SignUpMemberResponse(member.getId(), token);
    }

    @Transactional
    public RefreshResponse refresh(String oldToken) {
        Token newToken = tokenService.rotateRefreshToken(oldToken);
        return new RefreshResponse(newToken.accessToken());
    }

    public void logout(String oldToken) {
        tokenService.revokeRefreshToken(oldToken);
    }

    @Transactional
    public void deleteAccount(Member loginMember, WithdrawalRequest request, String oldToken) {
        loginMember.deleteAccount();
        withdrawalRecordWriter.store(
                WithdrawalRecord.create(request.withdrawalReason(), loginMember.getEmail(), loginMember.getNickname()));

        tokenService.revokeAllRefreshTokens(oldToken);
    }

    private void validateNotWithdrawnMember(String email) {
        if (withdrawalRecordReader.existEmail(email)) {
            throw new InvalidParamException(ErrorCode.MEMBER_WITHDRAWN);
        }
    }

    private void validateSignUpRequest(SignUpMemberRequest signUpRequest) {
        if (memberReader.existActiveNickname(signUpRequest.nickname())) {
            throw new InvalidParamException(ErrorCode.MEMBER_NICKNAME_DUPLICATE);
        }

        if (memberReader.existActiveEmail(signUpRequest.email())) {
            throw new InvalidParamException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }
    }
}
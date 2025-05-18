package com.juu.juulabel.auth.service;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.dto.request.OAuthLoginRequest;
import com.juu.juulabel.common.dto.response.LoginResponse;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.factory.OAuthProviderFactory;
import com.juu.juulabel.common.provider.JwtTokenProvider;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.repository.WithdrawalRecordReader;
import com.juu.juulabel.member.request.OAuthLoginInfo;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.request.OAuthUserInfo;
import com.juu.juulabel.member.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthProviderFactory providerFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberReader memberReader;
    private final WithdrawalRecordReader withdrawalRecordReader;

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

        Token token = memberOpt.map(member -> {
            String generatedToken = jwtTokenProvider.createAccessToken(member);
            return new Token(generatedToken, jwtTokenProvider.getExpirationByToken(generatedToken));
        }).orElse(new Token(null, null));

        return new LoginResponse(
                token,
                isNewMember,
                new OAuthUserInfo(
                        memberOpt.map(Member::getId).orElse(null),
                        email,
                        oAuthUser.id(),
                        provider));
    }

    private void validateNotWithdrawnMember(String email) {
        if (withdrawalRecordReader.existEmail(email)) {
            throw new InvalidParamException(ErrorCode.MEMBER_WITHDRAWN);
        }
    }
}
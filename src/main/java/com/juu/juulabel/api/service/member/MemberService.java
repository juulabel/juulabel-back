package com.juu.juulabel.api.service.member;

import com.juu.juulabel.api.dto.response.LoginResponse;
import com.juu.juulabel.api.factory.OAuthProviderFactory;
import com.juu.juulabel.api.provider.JwtTokenProvider;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.domain.dto.member.OAuthLoginInfo;
import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.token.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final OAuthProviderFactory providerFactory;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse login(OAuthLoginInfo authLoginInfo) {
        // 인가 코드를 이용해 액세스 토큰 발급 요청
        String accessToken = providerFactory.getAccessToken(
            authLoginInfo.provider(),
            authLoginInfo.propertyMap().get(AuthConstants.REDIRECT_URI),
            authLoginInfo.propertyMap().get(AuthConstants.CODE)
        );

        // 액세스 토큰을 이용해 사용자 정보 가져오기
        OAuthUser oAuthUser = providerFactory.getOAuthUser(authLoginInfo.provider(), accessToken);
        String providerId = oAuthUser.id();

        // TODO : 회원가입 or 로그인

        // 액세스 토큰 발급
        String token = jwtTokenProvider.createAccessToken(oAuthUser.email());

        return new LoginResponse(new Token(token, jwtTokenProvider.getExpirationByToken(token)));
    }

}

package com.juu.juulabel.common.factory;

import com.juu.juulabel.common.provider.oauth.GoogleProvider;
import com.juu.juulabel.common.provider.oauth.KakaoProvider;
import com.juu.juulabel.common.provider.oauth.OAuthProvider;
import com.juu.juulabel.common.dto.request.OAuthLoginRequest;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.token.OAuthToken;

import lombok.RequiredArgsConstructor;

import com.juu.juulabel.member.domain.Provider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthProviderFactory {

    private final KakaoProvider kakaoProvider;
    private final GoogleProvider googleProvider;

    private OAuthProvider getOAuthProvider(Provider provider) {
        return switch (provider) {
            case KAKAO -> kakaoProvider;
            case GOOGLE -> googleProvider;
            default -> throw new InvalidParamException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
        };
    }

    public OAuthUser getOAuthUser(OAuthLoginRequest oAuthLoginRequest) {
        Provider provider = oAuthLoginRequest.provider();
        String accessToken = getOAuthToken(
                provider,
                oAuthLoginRequest.redirectUri(),
                oAuthLoginRequest.code()).accessToken();

        return getOAuthProvider(provider).getOAuthUser(accessToken);
    }

    private OAuthToken getOAuthToken(Provider provider, String redirectUri, String code) {
        return getOAuthProvider(provider).getOAuthToken(redirectUri, code);
    }

}

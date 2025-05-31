package com.juu.juulabel.common.factory;

import com.juu.juulabel.common.provider.oauth.GoogleProvider;
import com.juu.juulabel.common.provider.oauth.KakaoProvider;
import com.juu.juulabel.common.provider.oauth.OAuthProvider;
import com.juu.juulabel.common.dto.request.OAuthLoginRequest;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.request.OAuthUser;

import com.juu.juulabel.member.domain.Provider;

import org.springframework.stereotype.Component;

@Component
public class OAuthProviderFactory {

    private final KakaoProvider kakaoProvider;
    private final GoogleProvider googleProvider;

    public OAuthProviderFactory(KakaoProvider kakaoProvider,
            GoogleProvider googleProvider) {
        this.kakaoProvider = kakaoProvider;
        this.googleProvider = googleProvider;
    }

    private OAuthProvider getOAuthProvider(Provider provider) {
        return switch (provider) {
            case KAKAO -> kakaoProvider;
            case GOOGLE -> googleProvider;
            default -> throw new InvalidParamException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
        };
    }

    public OAuthUser getOAuthUser(OAuthLoginRequest request) {

        System.out.println("request.redirectUri() = " + request.redirectUri());
        System.out.println("request.code() = " + request.code());

        Provider provider = request.provider();
        String accessToken = getOAuthProvider(provider)
                .getOAuthToken(request.redirectUri(), request.code())
                .accessToken();

        return getOAuthProvider(provider)
                .getOAuthUser(accessToken);

    }
}

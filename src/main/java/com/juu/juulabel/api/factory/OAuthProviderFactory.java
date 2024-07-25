package com.juu.juulabel.api.factory;

import com.juu.juulabel.api.provider.oauth.GoogleProvider;
import com.juu.juulabel.api.provider.oauth.KakaoProvider;
import com.juu.juulabel.api.provider.oauth.OAuthProvider;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.token.OAuthToken;
import com.juu.juulabel.domain.enums.member.Provider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Component
public class OAuthProviderFactory {

    private final Map<Provider, OAuthProvider> OAuthProviderMap;
    private final KakaoProvider kakaoProvider;
    private final GoogleProvider googleProvider;

    public OAuthProviderFactory(
            KakaoProvider kakaoProvider,
            GoogleProvider googleProvider
    ) {
        OAuthProviderMap = new EnumMap<>(Provider.class);
        this.kakaoProvider = kakaoProvider;
        this.googleProvider = googleProvider;
        initialize();
    }

    private void initialize() {
        OAuthProviderMap.put(Provider.KAKAO, kakaoProvider);
        OAuthProviderMap.put(Provider.GOOGLE, googleProvider);
    }

    private OAuthProvider getOAuthProvider(Provider provider) {
        OAuthProvider oAuthProvider = OAuthProviderMap.get(provider);
        if (Objects.isNull(oAuthProvider)) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_OAUTH_PROVIDER);
        }

        return oAuthProvider;
    }

    public String getAccessToken(Provider provider, String redirectUri, String code) {
        return getOAuthToken(provider, redirectUri, code).accessToken();
    }

    private OAuthToken getOAuthToken(Provider provider, String redirectUri, String code) {
        return getOAuthProvider(provider).getOAuthToken(redirectUri, code);
    }

    public String getProviderId(OAuthUser oAuthUser) {
        return oAuthUser.id();
    }

    public String getEmail(OAuthUser oAuthUser) {
        return oAuthUser.email();
    }

    public OAuthUser getOAuthUser(Provider provider, String accessToken) {
        return getOAuthProvider(provider).getOAuthUser(accessToken);
    }

}

package com.juu.juulabel.api.factory;

import com.juu.juulabel.api.provider.oauth.KakaoProvider;
import com.juu.juulabel.api.provider.oauth.OAuthProvider;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.token.OAuthToken;
import com.juu.juulabel.domain.enums.Provider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Component
public class OAuthProviderFactory {

    private final Map<Provider, OAuthProvider> OAuthProviderMap;
    private final KakaoProvider kakaoProvider;

    public OAuthProviderFactory(
            KakaoProvider kakaoProvider
    ) {
        OAuthProviderMap = new EnumMap<>(Provider.class);
        this.kakaoProvider = kakaoProvider;
        initialize();
    }

    private void initialize() {
        OAuthProviderMap.put(Provider.KAKAO, kakaoProvider);
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

    public Long getProviderId(Provider provider, String accessToken) {
        return getOAuthUser(provider, accessToken).id();
    }

    private OAuthToken getOAuthToken(Provider provider, String redirectUri, String code) {
        return getOAuthProvider(provider).getOAuthToken(redirectUri, code);
    }

    private OAuthUser getOAuthUser(Provider provider, String accessToken) {
        return getOAuthProvider(provider).getOAuthUser(accessToken);
    }

}

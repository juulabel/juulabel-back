package com.juu.juulabel.api.provider.oauth;

import com.juu.juulabel.api.client.KakaoApiClient;
import com.juu.juulabel.api.client.KakaoAuthClient;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoProvider implements OAuthProvider {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthClient kakaoAuthClient;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Override
    public OAuthToken getOAuthToken(String redirectUri, String code) {
        return kakaoAuthClient.generateOAuthToken(
                grantType,
                clientId,
                redirectUri,
                code,
                clientSecret
        );
    }

    @Override
    public OAuthUser getOAuthUser(String accessToken) {
        return kakaoApiClient.getUserInfo(getBearerToken(accessToken));
    }

    private String getBearerToken(String accessToken) {
        return AuthConstants.TOKEN_PREFIX + accessToken;
    }

}

package com.juu.juulabel.api.provider.oauth;

import com.juu.juulabel.api.client.GoogleApiClient;
import com.juu.juulabel.api.client.GoogleAuthClient;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GoogleProvider implements OAuthProvider {

    private final GoogleApiClient googleApiClient;
    private final GoogleAuthClient googleAuthClient;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    @Override
    public OAuthToken getOAuthToken(String redirectUri, String code) {
        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8); // 구글 oauth 서버로부터 받은 인가코드는 디코딩 해줘야 함
        return googleAuthClient.generateOAuthToken(
            decodedCode,
            clientId,
            clientSecret,
            redirectUri,
            grantType
        );
    }

    @Override
    public OAuthUser getOAuthUser(String accessToken) {
        return googleApiClient.getUserInfo(getBearerToken(accessToken));
    }

    private String getBearerToken(String accessToken) {
        return AuthConstants.TOKEN_PREFIX + accessToken;
    }

}

package com.juu.juulabel.common.provider.oauth;

import com.juu.juulabel.common.client.GoogleApiClient;
import com.juu.juulabel.common.client.GoogleAuthClient;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.token.OAuthToken;
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
                grantType);
    }

    @Override
    public OAuthUser getOAuthUser(OAuthToken oauthToken) {
        String accessToken = getBearerToken(oauthToken.accessToken());        
        return googleApiClient.getUserInfo(accessToken);
    }

}

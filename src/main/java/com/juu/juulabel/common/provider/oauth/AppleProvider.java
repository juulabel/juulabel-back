package com.juu.juulabel.common.provider.oauth;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juu.juulabel.common.client.AppleAuthClient;
import com.juu.juulabel.auth.service.AppleTokenService;
import com.juu.juulabel.member.request.ApplePublicKey;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.token.OAuthToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleProvider implements OAuthProvider {

    private final AppleAuthClient appleAuthClient;
    private final AppleTokenService appleTokenService;

    @Value("${spring.security.oauth2.client.registration.apple.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.apple.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.apple.clientSecret}")
    private String clientSecret;

    @Override
    public OAuthToken getOAuthToken(String redirectUri, String code) {
        return appleAuthClient.generateOAuthToken(
                code,
                clientId,
                clientSecret,
                redirectUri,
                grantType);
    }

    @Override
    public OAuthUser getOAuthUser(OAuthToken oauthToken) {
        List<ApplePublicKey> publicKeys = appleAuthClient.getApplePublicKeys();

        return appleTokenService.extractAppleUser(publicKeys, oauthToken);
    }

}

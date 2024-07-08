package com.juu.juulabel.api.provider.oauth;

import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.token.OAuthToken;

public interface OAuthProvider {

    OAuthToken getOAuthToken(String redirectUri, String code);

    OAuthUser getOAuthUser(String accessToken);
}

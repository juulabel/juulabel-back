package com.juu.juulabel.common.provider.oauth;

import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.token.OAuthToken;

public interface OAuthProvider {

    OAuthToken getOAuthToken(String redirectUri, String code);

    OAuthUser getOAuthUser(String accessToken);
}

package com.juu.juulabel.member.request;

import com.juu.juulabel.member.domain.Provider;

public interface OAuthUser {
    String id();
    String email();
    Provider provider();
}

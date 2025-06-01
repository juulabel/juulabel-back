package com.juu.juulabel.member.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juu.juulabel.member.domain.Provider;

public record GoogleUser(
    @JsonProperty("id") String id,
    @JsonProperty("email") String email,
    @JsonProperty("verified_email") boolean verifiedEmail,
    @JsonProperty("picture") String picture
) implements OAuthUser{

    @Override
    public Provider provider() {
        return Provider.GOOGLE;
    }

}
package com.juu.juulabel.member.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUser(
    @JsonProperty("id") String id,
    @JsonProperty("email") String email,
    @JsonProperty("verified_email") boolean verifiedEmail,
    @JsonProperty("picture") String picture
) implements OAuthUser{

}
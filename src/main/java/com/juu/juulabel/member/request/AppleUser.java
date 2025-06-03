package com.juu.juulabel.member.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juu.juulabel.member.domain.Provider;

public record AppleUser(
                @JsonProperty("id") String id,
                @JsonProperty("email") String email) implements OAuthUser {

        @Override
        public Provider provider() {
                return Provider.APPLE;
        }

}

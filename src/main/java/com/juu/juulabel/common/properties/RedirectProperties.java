package com.juu.juulabel.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

import com.juu.juulabel.member.domain.Provider;

@Data
@Component
@ConfigurationProperties(prefix = "app.redirect")
public class RedirectProperties {

    private String baseServer;
    private String baseClient;
    private String callback;
    private String login;
    private String signup;
    private String error;

    public String getRedirectUrl(Provider provider) {
        return baseServer + callback + "/" + provider.name().toLowerCase();
    }

    public String getLoginUrl() {
        return baseClient + login;
    }

    public String getSignupUrl() {
        return baseClient + signup;
    }

    public String getErrorUrl() {
        return baseClient + error;
    }

}

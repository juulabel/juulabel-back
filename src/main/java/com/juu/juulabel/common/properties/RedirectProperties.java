package com.juu.juulabel.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.common.http.RequestDataExtractor;

@Data
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.redirect")
public class RedirectProperties {

    private final RequestDataExtractor requestDataExtractor;

    private String localServer;
    private String localClient;
    private String remoteServer;
    private String remoteClient;
    private String callback;
    private String login;
    private String signup;
    private String error;

    public String getRedirectUrl(Provider provider) {
        return getContextAwareServerUrl() + callback + "/" + provider.name().toLowerCase();
    }

    public String getLoginUrl() {
        return getContextAwareClientUrl() + login;
    }

    public String getSignupUrl(String email) {
        return getContextAwareClientUrl() + signup + "?email=" + email;
    }

    public String getErrorUrl() {
        return getContextAwareClientUrl() + error;
    }

    // Example method using RequestDataExtractor
    public String getContextAwareServerUrl() {
        if (requestDataExtractor.isLocalRequest()) {
            return localServer;
        }
        return remoteServer;
    }

    public String getContextAwareClientUrl() {
        if (requestDataExtractor.isLocalRequest()) {
            return localClient;
        }
        return remoteClient;
    }

}

package com.juu.juulabel.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.cookie")
public class CookieProperties {
    private boolean secure;

    public boolean isSecure() {
        return secure;
    }
}

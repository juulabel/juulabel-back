package com.juu.juulabel.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for cookie management.
 * Allows customization of cookie security settings through application
 * properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.cookie")
public class CookieProperties {

    /**
     * Whether cookies should be marked as secure (HTTPS only).
     * Default: false (for development)
     */
    private boolean secure = false;

    /**
     * Default domain for cookies.
     * Default: juulabel.com
     */
    private String domain = "juulabel.com";

    /**
     * Default path for cookies.
     * Default: /app
     */
    private String path = "/app";

    /**
     * Default SameSite attribute for secure cookies.
     * Options: None, Lax, Strict
     * Default: None (for cross-site requests)
     */
    private String sameSiteSecure = "None";

    /**
     * Default SameSite attribute for non-secure cookies.
     * Options: None, Lax, Strict
     * Default: Lax (balanced security and functionality)
     */
    private String sameSiteNonSecure = "Lax";

    /**
     * Whether to set HttpOnly flag on cookies by default.
     * Default: true (recommended for security)
     */
    private boolean httpOnly = true;

    public boolean isSecure() {
        return secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }
}

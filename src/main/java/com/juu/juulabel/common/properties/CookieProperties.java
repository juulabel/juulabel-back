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
    private boolean secure;

    /**
     * Default domain for cookies.
     * Default: juulabel.com
     */
    private String domain;

    /**
     * Default path for cookies.
     * Default: /app
     */
    private String path;

    /**
     * Default SameSite attribute for secure cookies.
     * Options: None, Lax, Strict
     * Default: None (for cross-site requests)
     */
    private String sameSiteSecure;

    /**
     * Default SameSite attribute for non-secure cookies.
     * Options: None, Lax, Strict
     * Default: Lax (balanced security and functionality)
     */
    private String sameSiteNonSecure;

    /**
     * Whether to set HttpOnly flag on cookies by default.
     * Default: true (recommended for security)
     */
    private boolean httpOnly;

    public boolean isSecure() {
        return secure;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public String getSameSite() {
        return isSecure() ? sameSiteSecure : sameSiteNonSecure;
    }
}

package com.juu.juulabel.auth.domain;

import java.util.Arrays;
import java.util.Optional;

public enum ClientId {
    WEB("web-client"),
    IOS("ios-app"),
    ANDROID("android-app"),
    ADMIN("admin-panel");

    private final String value;

    ClientId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<ClientId> from(String value) {
        return Arrays.stream(values())
                .filter(c -> c.value.equals(value))
                .findFirst();
    }
}

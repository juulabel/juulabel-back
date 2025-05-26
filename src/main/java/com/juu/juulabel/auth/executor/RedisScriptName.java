package com.juu.juulabel.auth.executor;

public enum RedisScriptName {
    ROTATE_REFRESH_TOKEN("RotateRefreshTokenScriptExecutor"),
    LOGIN_REFRESH_TOKEN("LoginRefreshTokenScriptExecutor"),
    SAVE_REFRESH_TOKEN("SaveRefreshTokenScriptExecutor"),
    REVOKE_REFRESH_TOKEN_BY_INDEX_KEY("RevokeRefreshTokenByIndexKeyExecutor");

    private final String executorName;

    RedisScriptName(String name) {
        this.executorName = name;
    }

    public String getExecutorName() {
        return executorName;
    }
}

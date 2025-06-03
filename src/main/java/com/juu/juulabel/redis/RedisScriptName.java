package com.juu.juulabel.redis;

public enum RedisScriptName {

    ;

    private final String executorName;

    RedisScriptName(String name) {
        this.executorName = name;
    }

    public String getExecutorName() {
        return executorName;
    }
}

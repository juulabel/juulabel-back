package com.juu.juulabel.auth.executor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ScriptRegistry {

    private final Map<String, RedisScriptExecutor<?, ?>> scripts;

    public ScriptRegistry(List<RedisScriptExecutor<?, ?>> executors) {
        this.scripts = executors.stream()
                .collect(Collectors.toMap(e -> e.getClass().getSimpleName(), Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public <T, R> RedisScriptExecutor<T, R> get(RedisScriptName name) {
        return (RedisScriptExecutor<T, R>) scripts.get(name.getExecutorName());
    }
}
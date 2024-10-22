package com.juu.juulabel.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(memberId)) // memberId로 시작하는 이벤트들 필터링
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); // 필터링된 이벤트를 Map으로 반환
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(memberId))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    public void deleteEventCache(String cacheKey) {
        eventCache.remove(cacheKey);
    }
}

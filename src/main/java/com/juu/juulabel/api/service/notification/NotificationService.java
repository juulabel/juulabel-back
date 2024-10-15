package com.juu.juulabel.api.service.notification;

import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(Member member, String lastEventId) {
        Long memberId = member.getId();
        String emitterId = makeTimeIncludeId(memberId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> {
            emitter.complete();
            emitterRepository.deleteById(emitterId);
        });

        // 첫 연결 시 503 Service Unavailable 방지용 더미 Event 전송
        sendToClient(emitter, emitterId, "알림 서버 연결 성공. [memberId=" + memberId + "]");

        if (!lastEventId.isEmpty()) {
            sendLostData(lastEventId, memberId, emitter);
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                .id(emitterId)
                .name("sse")
                .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            emitter.completeWithError(exception);
        }
    }

    private void sendLostData(String lastEventId, Long memberId, SseEmitter emitter) {
        // eventCache에서 해당 memberId로 시작하는 이벤트들 가져오기
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        // lastEventId 이후의 이벤트만 필터링
        eventCaches.entrySet().stream()
            .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0) // lastEventId 이후의 이벤트만
            .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue())); // 필터링된 이벤트를 클라이언트로 전송
    }

    private String makeTimeIncludeId(Long memberId) { // (3)
        return memberId + "_" + System.currentTimeMillis();
    }
}

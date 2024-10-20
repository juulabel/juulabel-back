package com.juu.juulabel.api.service.notification;

import com.juu.juulabel.api.dto.request.CreateNotificationRequest;
import com.juu.juulabel.api.dto.request.NotificationListRequest;
import com.juu.juulabel.api.dto.response.NotificationListResponse;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.notification.NotificationSummary;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.notification.Notification;
import com.juu.juulabel.domain.enums.member.MemberRole;
import com.juu.juulabel.domain.enums.notification.NotificationType;
import com.juu.juulabel.domain.repository.EmitterRepository;
import com.juu.juulabel.domain.repository.reader.MemberReader;
import com.juu.juulabel.domain.repository.reader.NotificationReader;
import com.juu.juulabel.domain.repository.writer.NotificationWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final MemberReader memberReader;
    private final NotificationWriter notificationWriter;
    private final NotificationReader notificationReader;

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

    @Transactional
    public void sendNotificationToAllUsers(Member loginMember, CreateNotificationRequest request) {
        validateAdmin(loginMember);
        List<Member> allMembers = memberReader.getActiveMembers();
        allMembers.forEach(member -> CompletableFuture.runAsync(
            () -> sendAsync(member, request.notificationType(), request.content(), request.url())));
    }

    private void validateAdmin(Member loginMember) {
        if (loginMember.getRole() != MemberRole.ROLE_ADMIN) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_ADMIN);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAsync(Member receiver, NotificationType notificationType, String content, String url) {
        send(receiver, notificationType, content, url);
        log.info("Thread: {}, Notification sent to user: {}, type: {}, content: {}, url: {}",
            Thread.currentThread().getName(), receiver.getId(), notificationType, content, url);
    }

    @Transactional
    public void send(Member receiver, NotificationType notificationType, String content, String url) {
        Notification notification = notificationWriter.save(Notification.create(receiver, notificationType, content, url));
        Long receiverId = receiver.getId();
        String eventId = makeTimeIncludeId(receiverId);
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(String.valueOf(receiverId));
        emitters.forEach(
            (key, emitter) -> {
                emitterRepository.saveEventCache(key, notification);
                sendToClient(emitter, eventId,
                    new NotificationSummary(
                        notification.getId(),
                        notification.getRelatedUrl(),
                        notification.getContent(),
                        notification.getNotificationType(),
                        notification.isRead(),
                        notification.getCreatedAt()
                    ));
            }
        );
    }

    @Transactional
    public void sendDailyLifeLikeNotification(Member author, Member liker, Long dailyLifeId) {
        String message = liker.getNickname() + "님이 내 게시물에 좋아요를 눌렀어요.";
        NotificationType type = NotificationType.DAILY_LIFE_LIKE;
        String relatedUrl = "/v1/api/daily-lives/" + dailyLifeId;

        send(author, type, message, relatedUrl);
    }

    public void deleteDailyLifeLikeNotification(Member author, Member liker, Long dailyLifeId) {
        String content = liker.getNickname() + "님이 내 게시물에 좋아요를 눌렀어요.";
        String relatedUrl = "/v1/api/daily-lives/" + dailyLifeId;

        notificationWriter.deleteByReceiverAndContentAndRelatedUrl(author, content, relatedUrl);
        emitterRepository.deleteEventCache(author.getId() + "_" + relatedUrl);
    }

    @Transactional(readOnly = true)
    public NotificationListResponse getNotifications(Member member, NotificationListRequest request) {
        Slice<NotificationSummary> notificationList =
            notificationReader.getAllByMemberId(member.getId(), request.lastNotificationId(), request.pageSize());
        return new NotificationListResponse(notificationList);
    }

    @Transactional
    public void setNotificationsAsRead(Member member, Long notificationId) {
        notificationReader.setNotificationsAsRead(member, notificationId);
    }

    @Transactional
    public void setAllNotificationsAsRead(Member member) {
        notificationReader.setAllNotificationsAsRead(member);
    }

    @Transactional
    public void deleteNotification(Member member, Long notificationId) {
        notificationWriter.deleteNotification(member, notificationId);
    }

    @Transactional
    public void deleteAllNotifications(Member member) {
        notificationWriter.deleteAllByMember(member);
    }
}

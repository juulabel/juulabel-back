package com.juu.juulabel.notification.controller;

import com.juu.juulabel.notification.request.CreateNotificationRequest;
import com.juu.juulabel.common.dto.request.NotificationListRequest;
import com.juu.juulabel.common.dto.response.NotificationListResponse;
import com.juu.juulabel.notification.service.NotificationService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(
    name = "알림 API",
    description = "알림 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api/notifications"})
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
        summary = "알림 구독",
        description = "알림을 위한 SSE 구독"
    )
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
        @AuthenticationPrincipal Member member,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
        HttpServletResponse response
    ) {
        return notificationService.subscribe(member, lastEventId, response);
    }

    @Operation(
        summary = "전체 사용자 알림 전송",
        description = "관리자가 모든 사용자에게 알림 메시지를 전송합니다."
    )
    @PostMapping("/users")
    public ResponseEntity<CommonResponse<Void>> sendToAllUsers(
        @AuthenticationPrincipal Member member,
        @RequestBody @Valid CreateNotificationRequest request
    ) {
        notificationService.sendNotificationToAllUsers(member, request);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(
        summary = "알림 목록 조회",
        description = "사용자의 모든 알림 메시지를 조회합니다."
    )
    @GetMapping()
    public ResponseEntity<CommonResponse<NotificationListResponse>> getNotifications(
        @AuthenticationPrincipal Member member,
        @Valid NotificationListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, notificationService.getNotifications(member, request));
    }

    @Operation(
        summary = "알림 읽음 처리",
        description = "사용자가 클릭한 알림을 읽음 처리 합니다."
    )
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<CommonResponse<Void>> setNotificationAsRead(
        @AuthenticationPrincipal Member member,
        @PathVariable Long notificationId
    ) {
        notificationService.setNotificationsAsRead(member, notificationId);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(
        summary = "모든 알림 읽음 처리",
        description = "사용자의 모든 알림을 읽음 처리 합니다."
    )
    @PostMapping("/read-all")
    public ResponseEntity<CommonResponse<Void>> setAllNotificationAsRead(
        @AuthenticationPrincipal Member member
    ) {
        notificationService.setAllNotificationsAsRead(member);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(
        summary = "알림 삭제",
        description = "사용자가 선택한 알림을 삭제합니다."
    )
    @DeleteMapping("/{notificationId}/delete")
    public ResponseEntity<CommonResponse<Void>> deleteNotification(
        @AuthenticationPrincipal Member member,
        @PathVariable Long notificationId
    ) {
        notificationService.deleteNotification(member, notificationId);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

    @Operation(
        summary = "모든 알림 삭제",
        description = "사용자의 모든 알림을 삭제합니다."
    )
    @DeleteMapping("delete-all")
    public ResponseEntity<CommonResponse<Void>> deleteAllNotifications(
        @AuthenticationPrincipal Member member
    ) {
        notificationService.deleteAllNotifications(member);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

}

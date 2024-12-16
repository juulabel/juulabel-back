package com.juu.juulabel.api.controller.notification;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.CreateNotificationRequest;
import com.juu.juulabel.api.dto.request.NotificationListRequest;
import com.juu.juulabel.api.dto.response.NotificationListResponse;
import com.juu.juulabel.api.service.notification.NotificationService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
//        @Parameter(hidden = true) @LoginMember Member loginMember,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
        HttpServletResponse response
    ) {
        return notificationService.subscribe(lastEventId, response);
    }

    @Operation(
        summary = "전체 사용자 알림 전송",
        description = "관리자가 모든 사용자에게 알림 메시지를 전송합니다."
    )
    @PostMapping("/users")
    public ResponseEntity<CommonResponse<Void>> sendToAllUsers(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @RequestBody @Valid CreateNotificationRequest request
    ) {
        notificationService.sendNotificationToAllUsers(loginMember, request);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(
        summary = "알림 목록 조회",
        description = "사용자의 모든 알림 메시지를 조회합니다."
    )
    @GetMapping()
    public ResponseEntity<CommonResponse<NotificationListResponse>> getNotifications(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid NotificationListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, notificationService.getNotifications(loginMember, request));
    }

    @Operation(
        summary = "알림 읽음 처리",
        description = "사용자가 클릭한 알림을 읽음 처리 합니다."
    )
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<CommonResponse<Void>> setNotificationAsRead(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long notificationId
    ) {
        notificationService.setNotificationsAsRead(loginMember, notificationId);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(
        summary = "모든 알림 읽음 처리",
        description = "사용자의 모든 알림을 읽음 처리 합니다."
    )
    @PostMapping("/read-all")
    public ResponseEntity<CommonResponse<Void>> setAllNotificationAsRead(
        @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        notificationService.setAllNotificationsAsRead(loginMember);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(
        summary = "알림 삭제",
        description = "사용자가 선택한 알림을 삭제합니다."
    )
    @DeleteMapping("/{notificationId}/delete")
    public ResponseEntity<CommonResponse<Void>> deleteNotification(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long notificationId
    ) {
        notificationService.deleteNotification(loginMember, notificationId);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

    @Operation(
        summary = "모든 알림 삭제",
        description = "사용자의 모든 알림을 삭제합니다."
    )
    @DeleteMapping("delete-all")
    public ResponseEntity<CommonResponse<Void>> deleteAllNotifications(
        @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        notificationService.deleteAllNotifications(loginMember);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

}

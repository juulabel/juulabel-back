package com.juu.juulabel.api.controller.notification;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.service.notification.NotificationService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<CommonResponse<SseEmitter>> subscribe(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, notificationService.subscribe(loginMember, lastEventId));
    }

}

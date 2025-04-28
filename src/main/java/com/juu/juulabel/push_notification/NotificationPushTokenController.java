package com.juu.juulabel.push_notification;

import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "알림 푸시 토큰 API",
        description = "알림 푸시 토큰 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api/notifications/push-tokens"})
@RequiredArgsConstructor
public class NotificationPushTokenController {

    @Operation(
            summary = "유저 푸시 토큰 등록 및 업데이트",
            description = "푸시 알림을 위한 유저 푸시 토큰 등록 및 업데이트"
    )
    @PostMapping("/")
    public ResponseEntity  <CommonResponse<Void>> registerPushToken(
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS);
    }
}

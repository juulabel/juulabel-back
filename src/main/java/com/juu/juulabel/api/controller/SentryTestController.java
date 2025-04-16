package com.juu.juulabel.api.controller;


import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "테스트 API",
        description = "테스트 API"
)
@RestController
@RequestMapping("/sentry")
public class SentryTestController {


    @Operation(summary = "Sentry 알림 테스트 API")
    @GetMapping
    public void test() {
        try {
            throw new RuntimeException("222");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}

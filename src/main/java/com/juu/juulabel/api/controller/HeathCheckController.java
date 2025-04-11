package com.juu.juulabel.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "헬스체크 API",
        description = "헬스체크 API"
)
@RestController
public class HeathCheckController {


    @Operation(
            summary = "헬스체크 API",
            description = "AWS ALB에서 헬스체크"
    )
    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}

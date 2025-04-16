package com.juu.juulabel.api.controller;


import com.juu.juulabel.api.provider.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "테스트 API",
        description = "테스트 API"
)
@RestController
@RequiredArgsConstructor
public class TestAccessTokenController {

    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
            summary = "JWT 테스트용 토큰 발급 API",
            description = "기본 rldh11111@naver.com 이메일로 JWT 발급"
    )
    @GetMapping("/token")
    public String testAccessToken(@RequestParam(defaultValue = "rldh11111@naver.com") String email) {
        return jwtTokenProvider.createAccessToken(email);
    }

}

package com.juu.juulabel.api.controller.member;

import com.juu.juulabel.api.dto.request.GoogleLoginRequest;
import com.juu.juulabel.api.dto.request.KakaoLoginRequest;
import com.juu.juulabel.api.dto.response.LoginResponse;
import com.juu.juulabel.api.service.member.MemberService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/members"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "카카오 로그인")
    @PostMapping("/login/kakao")
    public ResponseEntity<CommonResponse<LoginResponse>> kakaoLogin(@Valid @RequestBody KakaoLoginRequest kakaoLoginRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.login(kakaoLoginRequest.toDto()));
    }

    @Operation(summary = "구글 로그인")
    @PostMapping("/login/google")
    public ResponseEntity<CommonResponse<LoginResponse>> googleLogin(@Valid @RequestBody GoogleLoginRequest googleLoginRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.login(googleLoginRequest.toDto()));
    }

}

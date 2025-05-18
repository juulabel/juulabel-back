package com.juu.juulabel.auth.controller;

import com.juu.juulabel.auth.aop.SetRefreshTokenCookie;
import com.juu.juulabel.auth.service.MemberAuthService;
import com.juu.juulabel.auth.service.OAuthService;
import com.juu.juulabel.auth.service.TokenService;
import com.juu.juulabel.common.dto.request.OAuthLoginRequest;
import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.dto.response.LoginResponse;
import com.juu.juulabel.common.dto.response.RefreshResponse;
import com.juu.juulabel.common.dto.response.SignUpMemberResponse;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "로그인, 회원가입, 토큰 관리 등 인증 관련 API")
@RestController
@RequestMapping(value = { "/v1/api/auth" })
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final OAuthService oAuthService;
    private final MemberAuthService memberAuthService;

    @Operation(summary = "OAuth 로그인 (소셜 로그인)")
    @PostMapping("/login/{provider}")
    @SetRefreshTokenCookie
    public ResponseEntity<CommonResponse<LoginResponse>> oauthLogin(
            @PathVariable String provider,
            @Valid @RequestBody OAuthLoginRequest requestBody) {

        // 경로에서 제공자 정보를 파싱하여 새 요청 객체를 생성
        OAuthLoginRequest request = new OAuthLoginRequest(
                requestBody.code(),
                requestBody.redirectUri(),
                Provider.valueOf(provider.toUpperCase()));

        LoginResponse loginResponse = oAuthService.login(request);
        return CommonResponse.success(SuccessCode.SUCCESS, loginResponse);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    @SetRefreshTokenCookie
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(
            @Valid @RequestBody SignUpMemberRequest request) {
        SignUpMemberResponse signUpMemberResponse = memberAuthService.signUp(request);
        return CommonResponse.success(SuccessCode.SUCCESS, signUpMemberResponse);
    }

    @Operation(summary = "액세스 토큰 갱신")
    @PostMapping("/refresh")
    @SetRefreshTokenCookie(parentTokenId = "#refreshToken", isNewSession = false)
    public ResponseEntity<CommonResponse<RefreshResponse>> refresh(
            @AuthenticationPrincipal Member member,
            @CookieValue(value = "refreshToken", required = true) String refreshToken) {
        return CommonResponse.success(SuccessCode.SUCCESS, tokenService.refresh(refreshToken));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(
            @CookieValue(value = "refreshToken", required = true) String refreshToken,
            @AuthenticationPrincipal Member member) {
        tokenService.logout(refreshToken, member.getId());
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResponseEntity<CommonResponse<Void>> deleteAccount(
            @AuthenticationPrincipal Member member,
            @RequestBody WithdrawalRequest request) {
        memberAuthService.deleteAccount(member, request);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

}
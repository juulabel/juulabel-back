package com.juu.juulabel.member.controller;

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
import com.juu.juulabel.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private final AuthService authService;

    @Operation(summary = "OAuth 로그인 (소셜 로그인)")
    @PostMapping("/login/{provider}")
    public ResponseEntity<CommonResponse<LoginResponse>> oauthLogin(
            @PathVariable String provider,
            @Valid @RequestBody OAuthLoginRequest requestBody,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        // 경로에서 제공자 정보를 파싱하여 새 요청 객체를 생성
        OAuthLoginRequest request = new OAuthLoginRequest(
                requestBody.code(),
                requestBody.redirectUri(),
                Provider.valueOf(provider.toUpperCase()));

        LoginResponse loginResponse = authService.login(request);
        if (!loginResponse.isNewMember()) {
            authService.registerRefreshToken(loginResponse.oAuthUserInfo().memberId(), httpServletRequest,
                    httpServletResponse);
        }
        return CommonResponse.success(SuccessCode.SUCCESS, loginResponse);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(
            @Valid @RequestBody SignUpMemberRequest request,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        SignUpMemberResponse signUpMemberResponse = authService.signUp(request);
        authService.registerRefreshToken(signUpMemberResponse.memberId(), httpServletRequest,
                httpServletResponse);
        return CommonResponse.success(SuccessCode.SUCCESS, signUpMemberResponse);
    }

    @Operation(summary = "액세스 토큰 갱신")
    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<RefreshResponse>> refresh(
            @CookieValue(value = "refreshToken", required = true) String refreshTokenCookie,
            HttpServletRequest request,
            HttpServletResponse response) {
        return CommonResponse.success(SuccessCode.SUCCESS, authService.refresh(refreshTokenCookie, request,
                response));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(
            @CookieValue(value = "refreshToken", required = true) String refreshTokenCookie) {
        authService.logout(refreshTokenCookie);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResponseEntity<CommonResponse<Void>> deleteAccount(
            @AuthenticationPrincipal Member member,
            @RequestBody WithdrawalRequest request) {
        authService.deleteAccount(member, request);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

    @Operation(summary = "닉네임 중복 검사")
    @GetMapping("/nicknames/{nickname}/exists")
    public ResponseEntity<CommonResponse<Boolean>> checkNickname(@PathVariable String nickname) {
        return CommonResponse.success(SuccessCode.SUCCESS, authService.checkNickname(nickname));
    }
}
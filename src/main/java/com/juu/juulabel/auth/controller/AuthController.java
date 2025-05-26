package com.juu.juulabel.auth.controller;

import com.juu.juulabel.auth.service.AuthService;
import com.juu.juulabel.common.constants.AuthConstants;
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

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @Override
    public ResponseEntity<CommonResponse<LoginResponse>> oauthLogin(
            @Parameter(description = "OAuth 제공자 (GOOGLE, KAKAO)", required = true) @PathVariable Provider provider,
            @Valid @RequestBody OAuthLoginRequest requestBody) {

        LoginResponse loginResponse = authService.login(requestBody);

        return CommonResponse.success(SuccessCode.SUCCESS, loginResponse);
    }

    @Override
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(
            @Valid @RequestBody SignUpMemberRequest request) {

        SignUpMemberResponse signUpMemberResponse = authService.signUp(request);

        return CommonResponse.success(SuccessCode.SUCCESS, signUpMemberResponse);
    }

    @Override
    public ResponseEntity<CommonResponse<RefreshResponse>> refresh(
            @Parameter(description = "리프레시 토큰 (쿠키)", required = true) @CookieValue(value = AuthConstants.REFRESH_TOKEN_HEADER_NAME, required = true) String refreshToken,
            @AuthenticationPrincipal Member member) {

        RefreshResponse refreshResponse = authService.refresh(refreshToken);

        return CommonResponse.success(SuccessCode.SUCCESS, refreshResponse);
    }

    @Override
    public ResponseEntity<CommonResponse<Void>> logout(
            @Parameter(description = "리프레시 토큰 (쿠키)", required = true) @CookieValue(value = AuthConstants.REFRESH_TOKEN_HEADER_NAME, required = true) String refreshToken,
            @AuthenticationPrincipal Member member) {

        authService.logout(refreshToken);

        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Override
    public ResponseEntity<CommonResponse<Void>> deleteAccount(
            @Parameter(description = "리프레시 토큰 (쿠키)", required = true) @CookieValue(value = AuthConstants.REFRESH_TOKEN_HEADER_NAME, required = true) String refreshToken,
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody WithdrawalRequest request) {

        authService.deleteAccount(member, request, refreshToken);

        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }
}
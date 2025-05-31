package com.juu.juulabel.auth.controller;

import com.juu.juulabel.auth.domain.SignUpToken;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @Override
    public ResponseEntity<CommonResponse<LoginResponse>> login(
            @PathVariable Provider provider,
            CsrfToken csrfToken,
            @Valid @RequestBody OAuthLoginRequest request) {
        csrfToken.getToken();

        return CommonResponse.success(SuccessCode.SUCCESS, authService.login(request));
    }

    @Override
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(
            @AuthenticationPrincipal SignUpToken signUpToken,
            @Valid @RequestBody SignUpMemberRequest request) {

        return CommonResponse.success(SuccessCode.SUCCESS, authService.signUp(signUpToken, request));
    }

    @Override
    public ResponseEntity<CommonResponse<RefreshResponse>> refresh(
            @CookieValue(value = AuthConstants.REFRESH_TOKEN_NAME, required = true) String refreshToken) {

        return CommonResponse.success(SuccessCode.SUCCESS, authService.refresh(refreshToken));
    }

    @Override
    public ResponseEntity<CommonResponse<Void>> logout(
            @AuthenticationPrincipal Member member) {

        authService.logout(member.getId());

        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Override
    public ResponseEntity<CommonResponse<Void>> deleteAccount(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody WithdrawalRequest request) {

        authService.deleteAccount(member, request);

        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }
}
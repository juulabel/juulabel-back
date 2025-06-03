package com.juu.juulabel.auth.controller;

import com.juu.juulabel.auth.service.AuthService;
import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;

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
    public ResponseEntity<CommonResponse<Void>> login(
            @PathVariable Provider provider,
            @RequestParam(required = true) String code,
            @RequestParam(required = true) String state) {
        
        authService.login(provider, code, state);

        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Override
    public ResponseEntity<CommonResponse<Void>> signUp(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody SignUpMemberRequest request) {

        authService.signUp(member, request);

        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @Override
    public ResponseEntity<CommonResponse<Void>> logout() {

        authService.logout();

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
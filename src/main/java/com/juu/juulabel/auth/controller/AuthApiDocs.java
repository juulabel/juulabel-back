package com.juu.juulabel.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "인증 API", description = "로그인, 회원가입, 회원탈퇴, 토큰 관리 등 인증 관련 API")
@RequestMapping("/v1/api/auth")
public interface AuthApiDocs {

        @Operation(summary = "OAuth 소셜 로그인 콜백", description = "지원되는 OAuth 제공자(Google, Kakao)를 통한 로그인 콜백")
        @ApiResponse(responseCode = "200", description = "로그인 성공", headers = {
                        @Header(name = "Set-Cookie", description = "계정이 존재할시만 리프레시 토큰 발급", schema = @Schema(type = "string"))
        })
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
        @ApiResponse(responseCode = "401", description = "인증 실패")
        @GetMapping("/oauth/callback/{provider}")
        public ResponseEntity<CommonResponse<Void>> login(                        
                        @Parameter(description = "OAuth 제공자 (GOOGLE, KAKAO)", required = true) @PathVariable Provider provider,
                        @RequestParam(required = true) String code,
                        @RequestParam(required = true) String state);

        @Operation(summary = "회원가입", description = "새로운 회원 등록 및 초기 토큰 발급")
        @ApiResponse(responseCode = "200", description = "회원가입 성공", headers = {
                        @Header(name = "Set-Cookie", description = "리프레시 토큰 발급", schema = @Schema(type = "string")),
        })
        @ApiResponse(responseCode = "400", description = "유효성 검사 실패, 중복된 이메일 또는 닉네임")
        @PostMapping("/sign-up")
        public ResponseEntity<CommonResponse<Void>> signUp(
                        @AuthenticationPrincipal Member member,
                        @Valid @RequestBody SignUpMemberRequest request);

        @Operation(summary = "로그아웃", description = "현재 디바이스의 리프레시 토큰 무효화")
        @ApiResponse(responseCode = "200", description = "로그아웃 성공", headers = {
                        @Header(name = "Set-Cookie", description = "리프레시 토큰 즉시 삭제", schema = @Schema(type = "string"))
        })
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
        @PostMapping("/logout")
        public ResponseEntity<CommonResponse<Void>> logout();

        @Operation(summary = "회원 탈퇴", description = "회원 계정 삭제 및 모든 토큰 무효화")
        @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", headers = {
                        @Header(name = "Set-Cookie", description = "리프레시 토큰 즉시 삭제", schema = @Schema(type = "string"))
        })
        @ApiResponse(responseCode = "400", description = "잘못된 탈퇴 요청")
        @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
        @DeleteMapping("/me")
        public ResponseEntity<CommonResponse<Void>> deleteAccount(
                        @AuthenticationPrincipal Member member,
                        @Valid @RequestBody WithdrawalRequest request);

}

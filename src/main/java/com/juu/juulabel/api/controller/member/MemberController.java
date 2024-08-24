package com.juu.juulabel.api.controller.member;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.OAuthLoginRequest;
import com.juu.juulabel.api.dto.request.SignUpMemberRequest;
import com.juu.juulabel.api.dto.request.UpdateProfileRequest;
import com.juu.juulabel.api.dto.response.LoginResponse;
import com.juu.juulabel.api.dto.response.SignUpMemberResponse;
import com.juu.juulabel.api.dto.response.UpdateProfileResponse;
import com.juu.juulabel.api.service.member.MemberService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = {"/v1/api/members"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "카카오 로그인")
    @PostMapping("/login/kakao")
    public ResponseEntity<CommonResponse<LoginResponse>> kakaoLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.login(oAuthLoginRequest));
    }

    @Operation(summary = "구글 로그인")
    @PostMapping("/login/google")
    public ResponseEntity<CommonResponse<LoginResponse>> googleLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.login(oAuthLoginRequest));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(@Valid @RequestBody SignUpMemberRequest signUpMemberRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.signUp(signUpMemberRequest));
    }

    @Operation(summary = "닉네임 중복 검사")
    @GetMapping("/nicknames/{nickname}/exists")
    public ResponseEntity<CommonResponse<Boolean>> checkNickname(@NotNull @PathVariable String nickname) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.checkNickname(nickname));
    }

    @Operation(summary = "프로필 수정")
    @PutMapping("/me/profile")
    public ResponseEntity<CommonResponse<UpdateProfileResponse>> updateProfile(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestPart(value = "request") UpdateProfileRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.updateProfile(loginMember, request, image));
    }

}

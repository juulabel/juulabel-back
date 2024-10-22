package com.juu.juulabel.api.controller.member;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.*;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.service.member.MemberService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(
    name = "회원 API",
    description = "로그인,회원가입,프로필 수정,내가 작성한 게시글 조회 등 회원 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api/members"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "카카오 로그인")
    @PostMapping("/login/kakao")
    public ResponseEntity<CommonResponse<LoginResponse>> kakaoLogin(@Valid @RequestBody OAuthLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.login(request));
    }

    @Operation(summary = "구글 로그인")
    @PostMapping("/login/google")
    public ResponseEntity<CommonResponse<LoginResponse>> googleLogin(@Valid @RequestBody OAuthLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.login(request));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(@Valid @RequestBody SignUpMemberRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.signUp(request));
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

    @Operation(summary = "내가 작성한 일상생활 목록 조회")
    @Parameters(@Parameter(name = "request", description = "내가 작성한 일상생활 목록 조회 요청", required = true))
    @GetMapping("/daily-lives/my")
    public ResponseEntity<CommonResponse<MyDailyLifeListResponse>> loadMyDailyLifeList(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid DailyLifeListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMyDailyLifeList(loginMember, request));
    }

    @Operation(summary = "내가 작성한 시음노트 목록 조회")
    @Parameters(@Parameter(name = "request", description = "내가 작성한 시음노트 목록 조회 요청", required = true))
    @GetMapping("/tasting_notes/my")
    public ResponseEntity<CommonResponse<MyTastingNoteListResponse>> loadMyTastingNoteList(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid TastingNoteListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMyTastingNoteList(loginMember, request));
    }

    @Operation(summary = "전통주 저장")
    @PostMapping("/{alcoholicDrinksId}/save")
    public ResponseEntity<CommonResponse<Void>> saveAlcoholicDrinks(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long alcoholicDrinksId
    ) {
        boolean isSaved = memberService.saveAlcoholicDrinks(loginMember, alcoholicDrinksId);
        return CommonResponse.success(isSaved ? SuccessCode.SUCCESS_INSERT : SuccessCode.SUCCESS_DELETE);
    }

    @Operation(summary = "내가 저장한 전통주 목록 조회")
    @Parameters(@Parameter(name = "request", description = "내가 저장한 전통주 목록 조회 요청", required = true))
    @GetMapping("/alcoholic-drinks/my")
    public ResponseEntity<CommonResponse<MyAlcoholicDrinksListResponse>> loadMyAlcoholicDrinks(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid MyAlcoholicDrinksListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMyAlcoholicDrinks(loginMember, request));
    }

    @Operation(summary = "내 공간 조회")
    @GetMapping("/my-space")
    public ResponseEntity<CommonResponse<MySpaceResponse>> getMySpace(@Parameter(hidden = true) @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMySpace(loginMember));
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/my-info")
    public ResponseEntity<CommonResponse<MyInfoResponse>> getMyInfo(@Parameter(hidden = true) @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMyInfo(loginMember));
    }

    @Operation(summary = "타 유저 프로필 조회")
    @GetMapping("/{memberId}/profile")
    public ResponseEntity<CommonResponse<MemberProfileResponse>> getMemberProfile(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long memberId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMemberProfile(loginMember, memberId));
    }

    @Operation(summary = "특정 회원이 작성한 시음노트 목록 조회")
    @Parameters(@Parameter(name = "request", description = "특정 회원이 작성한 시음노트 목록 조회 요청", required = true))
    @GetMapping("/{memberId}/tasting_notes")
    public ResponseEntity<CommonResponse<TastingNoteListResponse>> loadMemberTastingNoteList(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid TastingNoteListRequest request,
        @PathVariable Long memberId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMemberTastingNoteList(loginMember, request, memberId));
    }

    @Operation(summary = "특정 회원이 작성한 일상생활 목록 조회")
    @Parameters(@Parameter(name = "request", description = "특정 회원이 작성한 일상생활 목록 조회 요청", required = true))
    @GetMapping("/{memberId}/daily-lives")
    public ResponseEntity<CommonResponse<DailyLifeListResponse>> loadMemberDailyLifeList(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid DailyLifeListRequest request,
        @PathVariable Long memberId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMemberDailyLifeList(loginMember, request, memberId));
    }

}

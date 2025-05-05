package com.juu.juulabel.member.controller;

import com.juu.juulabel.common.dto.request.*;
import com.juu.juulabel.common.dto.response.*;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.dailylife.response.DailyLifeListRequest;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Tag(name = "회원 API", description = "프로필 수정, 내 정보 조회 등 회원 관련 API")
@RestController
@RequestMapping(value = { "/v1/api/members" })
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "프로필 수정")
    @PutMapping("/me/profile")
    public ResponseEntity<CommonResponse<UpdateProfileResponse>> updateProfile(
            @AuthenticationPrincipal Member member,
            @Valid @RequestPart(value = "request") UpdateProfileRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.updateProfile(member, request, image));
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/my-info")
    public ResponseEntity<CommonResponse<MyInfoResponse>> getMyInfo(@AuthenticationPrincipal Member member) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMyInfo(member));
    }

    @Operation(summary = "내 공간 조회")
    @GetMapping("/my-space")
    public ResponseEntity<CommonResponse<MySpaceResponse>> getMySpace(@AuthenticationPrincipal Member member) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMySpace(member));
    }

    @Operation(summary = "타 유저 프로필 조회")
    @GetMapping("/{memberId}/profile")
    public ResponseEntity<CommonResponse<MemberProfileResponse>> getMemberProfile(
            @AuthenticationPrincipal Member member,
            @PathVariable Long memberId) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMemberProfile(member, memberId));
    }

    @Operation(summary = "내가 작성한 일상생활 목록 조회")
    @Parameters(@Parameter(name = "request", description = "내가 작성한 일상생활 목록 조회 요청", required = true))
    @GetMapping("/daily-lives/my")
    public ResponseEntity<CommonResponse<MyDailyLifeListResponse>> loadMyDailyLifeList(
            @AuthenticationPrincipal Member member,
            @Valid DailyLifeListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMyDailyLifeList(member, request));
    }

    @Operation(summary = "내가 작성한 시음노트 목록 조회")
    @Parameters(@Parameter(name = "request", description = "내가 작성한 시음노트 목록 조회 요청", required = true))
    @GetMapping("/tasting-notes/my")
    public ResponseEntity<CommonResponse<MyTastingNoteListResponse>> loadMyTastingNoteList(
            @AuthenticationPrincipal Member member,
            @Valid TastingNoteListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMyTastingNoteList(member, request));
    }

    @Operation(summary = "전통주 저장")
    @PostMapping("/alcoholic-drinks/{alcoholicDrinksId}/save")
    public ResponseEntity<CommonResponse<Void>> saveAlcoholicDrinks(
            @AuthenticationPrincipal Member member,
            @PathVariable Long alcoholicDrinksId) {
        boolean isSaved = memberService.saveAlcoholicDrinks(member, alcoholicDrinksId);
        return CommonResponse.success(isSaved ? SuccessCode.SUCCESS_INSERT : SuccessCode.SUCCESS_DELETE);
    }

    @Operation(summary = "내가 저장한 전통주 목록 조회")
    @Parameters(@Parameter(name = "request", description = "내가 저장한 전통주 목록 조회 요청", required = true))
    @GetMapping("/alcoholic-drinks/my")
    public ResponseEntity<CommonResponse<MyAlcoholicDrinksListResponse>> loadMyAlcoholicDrinks(
            @AuthenticationPrincipal Member member,
            @Valid MyAlcoholicDrinksListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.loadMyAlcoholicDrinks(member, request));
    }

    @Operation(summary = "특정 회원이 작성한 시음노트 목록 조회")
    @Parameters(@Parameter(name = "request", description = "특정 회원이 작성한 시음노트 목록 조회 요청", required = true))
    @GetMapping("/members/{memberId}/tasting-notes")
    public ResponseEntity<CommonResponse<TastingNoteListResponse>> loadMemberTastingNoteList(
            @AuthenticationPrincipal Member member,
            @Valid TastingNoteListRequest request,
            @PathVariable Long memberId) {
        return CommonResponse.success(SuccessCode.SUCCESS,
                memberService.loadMemberTastingNoteList(member, request, memberId));
    }

    @Operation(summary = "특정 회원이 작성한 일상생활 목록 조회")
    @Parameters(@Parameter(name = "request", description = "특정 회원이 작성한 일상생활 목록 조회 요청", required = true))
    @GetMapping("/members/{memberId}/daily-lives")
    public ResponseEntity<CommonResponse<DailyLifeListResponse>> loadMemberDailyLifeList(
            @AuthenticationPrincipal Member member,
            @Valid DailyLifeListRequest request,
            @PathVariable Long memberId) {
        return CommonResponse.success(SuccessCode.SUCCESS,
                memberService.loadMemberDailyLifeList(member, request, memberId));
    }
}

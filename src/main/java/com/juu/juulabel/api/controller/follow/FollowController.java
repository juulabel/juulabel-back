package com.juu.juulabel.api.controller.follow;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.FollowOrUnfollowRequest;
import com.juu.juulabel.api.dto.request.FollowerListRequest;
import com.juu.juulabel.api.dto.request.FollowingListRequest;
import com.juu.juulabel.api.dto.response.FollowOrUnfollowResponse;
import com.juu.juulabel.api.dto.response.FollowerListResponse;
import com.juu.juulabel.api.dto.response.FollowingListResponse;
import com.juu.juulabel.api.service.follow.FollowService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "팔로우 API",
        description = "팔로우, 언팔로우 등 팔로우 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api"})
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @Operation(
            summary = "팔로우, 언팔로우",
            description = "회원을 팔로우 또는 언팔로우한다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팔로우, 언팔로우 성공"
    )
    @PostMapping("/follow")
    public ResponseEntity<CommonResponse<FollowOrUnfollowResponse>> followOrUnfollow(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @Valid @RequestBody FollowOrUnfollowRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.followOrUnfollow(loginMember, request));
    }

    @Operation(
            summary = "팔로잉 목록 조회",
            description = "회원의 팔로잉 목록을 조회한다."
    )
    @Parameters(
            @Parameter(name = "request", description = "팔로잉 리스트 조회 요청", required = true)
    )
    @GetMapping("/members/{memberId}/followings")
    public ResponseEntity<CommonResponse<FollowingListResponse>> loadFollowingList(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @PathVariable Long memberId,
            @Valid FollowingListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadFollowingList(loginMember, memberId, request));
    }

    @Operation(
            summary = "팔로워 목록 조회",
            description = "회원의 팔로워 목록을 조회한다."
    )
    @Parameters(
            @Parameter(name = "request", description = "팔로워 리스트 조회 요청", required = true)
    )
    @GetMapping("/members/{memberId}/followers")
    public ResponseEntity<CommonResponse<FollowerListResponse>> loadFollowerList(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @PathVariable Long memberId,
            @Valid FollowerListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadFollowerList(loginMember, memberId, request));
    }

}

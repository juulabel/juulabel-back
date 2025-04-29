package com.juu.juulabel.follow.controller;

import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.common.dto.request.RecommendListRequest;
import com.juu.juulabel.common.dto.request.SearchUserListRequest;
import com.juu.juulabel.common.dto.response.*;
import com.juu.juulabel.follow.service.FollowService;
import com.juu.juulabel.follow.response.FollowDeleteRequest;
import com.juu.juulabel.follow.response.FollowOrUnfollowRequest;
import com.juu.juulabel.follow.response.FollowerListRequest;
import com.juu.juulabel.follow.response.FollowingListRequest;
import com.juu.juulabel.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//@Hidden
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
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody FollowOrUnfollowRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.followOrUnfollow(member, request));
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
            @AuthenticationPrincipal Member member,
            @PathVariable Long memberId,
            @Valid FollowingListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadFollowingList(member, memberId, request));
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
            @AuthenticationPrincipal Member member,
            @PathVariable Long memberId,
            @Valid FollowerListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadFollowerList(member, memberId, request));
    }

    @Operation(
            summary = "유저 검색",
            description = "검색 된 유저 목록을 조회한다."
    )
    @Parameters(
            @Parameter(name = "request", description = "유저 목록 조회 요청", required = true)
    )
    @GetMapping("/members/search")
    public ResponseEntity<CommonResponse<SearchUserListResponse>> loadSearchUserList(
            @AuthenticationPrincipal Member member,
            @Valid SearchUserListRequest request ) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadSearchUserList(member, request));
    }

    @Operation(
            summary = "팔로워 삭제",
            description = "팔로워를 강제로 삭제한다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팔로워 삭제 성공"
    )
    @PostMapping("/delete/following")
    public ResponseEntity<CommonResponse<FollowDeleteResponse>> deleteFollowing(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody FollowDeleteRequest request){
        return CommonResponse.success(SuccessCode.SUCCESS, followService.deleteFollowing(member, request));
    }

    @Operation(
            summary = "유저 추천",
            description = "유저 추천"
    )
    @GetMapping("/members/recommendations")
    public ResponseEntity<CommonResponse<RecommendListResponse>> loadRecommendList(
            @AuthenticationPrincipal Member member,
            @Valid RecommendListRequest request
    ){
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadRecommendList(member,request));
    }

}

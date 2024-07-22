package com.juu.juulabel.api.controller.follow;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.FollowOrUnfollowRequest;
import com.juu.juulabel.api.dto.request.LoadFollowerListRequest;
import com.juu.juulabel.api.dto.request.LoadFollowingListRequest;
import com.juu.juulabel.api.dto.response.FollowOrUnfollowResponse;
import com.juu.juulabel.api.dto.response.LoadFollowerListResponse;
import com.juu.juulabel.api.dto.response.LoadFollowingListResponse;
import com.juu.juulabel.api.service.follow.FollowService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/v1/api"})
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우, 언팔로우")
    @PostMapping("/follow")
    public ResponseEntity<CommonResponse<FollowOrUnfollowResponse>> followOrUnfollow(
            @LoginMember Member loginMember,
            @Valid @RequestBody FollowOrUnfollowRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.followOrUnfollow(loginMember, request));
    }

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/members/{memberId}/followings")
    public ResponseEntity<CommonResponse<LoadFollowingListResponse>> loadFollowingList(
            @LoginMember Member loginMember,
            @PathVariable Long memberId,
            LoadFollowingListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadFollowingList(loginMember, memberId, request));
    }

    @Operation(summary = "팔로워 목록 조회")
    @PostMapping("/members/{memberId}/followers")
    public ResponseEntity<CommonResponse<LoadFollowerListResponse>> loadFollowerList(
            @LoginMember Member loginMember,
            @PathVariable Long memberId,
            LoadFollowerListRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, followService.loadFollowerList(loginMember, memberId, request));
    }

}

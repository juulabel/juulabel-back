package com.juu.juulabel.admin;

import com.juu.juulabel.admin.response.MemberListSummary;
import com.juu.juulabel.alcohol.response.CategorySearchAlcoholRequest;
import com.juu.juulabel.common.dto.request.MemberListRequest;
import com.juu.juulabel.common.dto.response.MemberListResponse;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "관리자 API",
    description = "뱃지 부여 및 알림 발송 등 관리자 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api/admins"})
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "뱃지 부여")
    @PostMapping("/badges")
    public ResponseEntity<CommonResponse<Void>> assignBadge(
        @AuthenticationPrincipal Member member,
        @RequestParam(value = "email") String email
    ) {
        adminService.assignBadge(email, member);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @GetMapping("/permission/test")
    public ResponseEntity<Member> test(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(member);
    }

    @GetMapping("/memberList")
    public ResponseEntity<CommonResponse<MemberListResponse>> loadMemberList( Member loginMember, @Valid MemberListRequest request) {
        return  CommonResponse.success(SuccessCode.SUCCESS, adminService.loadMemberList(loginMember, request));
    }
}

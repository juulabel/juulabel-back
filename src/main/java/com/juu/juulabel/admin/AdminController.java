package com.juu.juulabel.admin;

import com.juu.juulabel.common.annotation.LoginMember;
import com.juu.juulabel.common.dto.request.MemberListRequest;
import com.juu.juulabel.common.dto.response.MemberListResponse;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "관리자 API",
    description = "뱃지 부여 및 알림 발송 등 관리자 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api/admins"})
@RequiredArgsConstructor
public class AdminController {

    private final com.juu.juulabel.admin.AdminService adminService;

    @Operation(summary = "뱃지 부여")
    @PostMapping("/badges")
    public ResponseEntity<CommonResponse<Void>> assignBadge(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @RequestParam(value = "email") String email
    ) {
        adminService.assignBadge(email, loginMember);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }


    //### 🔹 **회원 리스트 (검색/필터)**
    //- 검색: 닉네임, 이메일, 가입일, 상태(활성/정지/경고)
    //- 필터: 가입 소셜 계정(이메일/카카오/네이버), 뱃지 보유 여부

    @Operation(summary = "회원리스트 조회")
    @GetMapping("/pages/members")
    public ResponseEntity<CommonResponse<MemberListResponse>> loadMemberList(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            MemberListRequest request
    ){
        return CommonResponse.success(SuccessCode.SUCCESS, adminService.loadMemberList(loginMember, request));
    }



}

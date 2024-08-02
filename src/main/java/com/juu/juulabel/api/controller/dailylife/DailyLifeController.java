package com.juu.juulabel.api.controller.dailylife;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.*;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.service.dailylife.DailyLifeService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(
    name = "일상생활 API",
    description = "게시글 조회,수정,삭제 등 일상생활 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api/daily-lives"})
@RequiredArgsConstructor
public class DailyLifeController {

    private final DailyLifeService dailyLifeService;

    @Operation(
        summary = "일상생활 작성",
        description = "전통주 일상생활 게시글을 작성한다."
    )
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WriteDailyLifeResponse>> write(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestPart(value = "request") WriteDailyLifeRequest request,
        @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, dailyLifeService.writeDailyLife(loginMember, request, files));
    }

    @Operation(
        summary = "일상생활 상세 조회",
        description = "전통주 일상생활 게시글을 상세 조회한다."
    )
    @GetMapping("/{dailyLifeId}")
    public ResponseEntity<CommonResponse<LoadDailyLifeResponse>> loadDailyLife(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long dailyLifeId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, dailyLifeService.loadDailyLife(loginMember, dailyLifeId));
    }

    @Operation(
        summary = "일상생활 목록 조회",
        description = "전통주 일상생활 게시글 목록을 조회한다."
    )
    @Parameters(
        @Parameter(name = "request", description = "일상생활 게시글 목록 조회 요청", required = true)
    )
    @GetMapping
    public ResponseEntity<CommonResponse<LoadDailyLifeListResponse>> loadDailyLifeList(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid LoadDailyLifeListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, dailyLifeService.loadDailyLifeList(loginMember, request));
    }

    @Operation(
        summary = "일상생활 수정",
        description = "전통주 일상생활 게시글을 수정한다."
    )
    @PatchMapping(value = "/{dailyLifeId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<updateDailyLifeResponse>> updateDailyLife(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestPart(value = "request") UpdateDailyLifeRequest request,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @PathVariable Long dailyLifeId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE, dailyLifeService.updateDailyLife(loginMember, dailyLifeId, request, files));
    }

    @Operation(
        summary = "일상생활 삭제",
        description = "전통주 일상생활 게시글을 삭제한다."
    )
    @DeleteMapping("/{dailyLifeId}")
    public ResponseEntity<CommonResponse<deleteDailyLifeResponse>> deleteDailyLife(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long dailyLifeId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, dailyLifeService.deleteDailyLife(loginMember, dailyLifeId));
    }

    @Operation(
        summary = "일상생활 댓글 작성",
        description = "전통주 일상생활 게시글에 댓글을 작성한다."
    )
    @PostMapping("/{dailyLifeId}/comments")
    public ResponseEntity<CommonResponse<WriteDailyLifeCommentResponse>> writeComment(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestBody WriteDailyLifeCommentRequest request,
        @PathVariable Long dailyLifeId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, dailyLifeService.writeComment(loginMember, request, dailyLifeId));
    }

    @Operation(
        summary = "일상생활 댓글 수정",
        description = "전통주 일상생활 게시글의 댓글을 수정한다."
    )
    @PatchMapping("/{dailyLifeId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<UpdateDailyLifeCommentResponse>> updateComment(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestBody UpdateDailyLifeCommentRequest request,
        @PathVariable Long dailyLifeId,
        @PathVariable Long commentId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, dailyLifeService.updateComment(loginMember, request, dailyLifeId, commentId));
    }


}

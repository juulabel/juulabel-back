package com.juu.juulabel.api.controller.dailylife;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.WriteDailyLifeRequest;
import com.juu.juulabel.api.dto.response.WriteDailyLifeResponse;
import com.juu.juulabel.api.service.dailylife.DailyLifeService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
        return CommonResponse.success(SuccessCode.SUCCESS, dailyLifeService.writeDailyLife(loginMember, request, files));
    }
}

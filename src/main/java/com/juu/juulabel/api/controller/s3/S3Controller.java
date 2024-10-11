package com.juu.juulabel.api.controller.s3;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.response.ImageUploadResponse;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(
    name = "이미지 파일 업로드 API",
    description = "프론트 개발 편의를 위한 이미지 개별 업로드 API"
)
@RestController
@RequestMapping(value = {"/v1/api/images"})
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "이미지 파일 업로드")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ImageUploadResponse>> uploadImage(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @RequestPart(value = "image") MultipartFile image
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, s3Service.uploadMemberImage(image));
    }

}

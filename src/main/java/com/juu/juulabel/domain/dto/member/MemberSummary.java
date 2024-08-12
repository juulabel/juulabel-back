package com.juu.juulabel.domain.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 요약 정보")
public record MemberSummary(
        @Schema(description = "회원 고유 번호", example = "38")
        Long id,
        @Schema(description = "회원 닉네임", example = "느린마을 거북이")
        String nickname,
        @Schema(description = "프로필 이미지 URL")
        String profileImage
) {
}

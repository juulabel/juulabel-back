package com.juu.juulabel.api.dto.request;

import com.juu.juulabel.domain.enums.member.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateProfileRequest(
    @NotBlank(message = "닉네임이 누락되었습니다.")
    String nickname,
    String introduction,
    @NotNull(message = "성별이 누락되었습니다.")
    Gender gender,
    @NotEmpty(message = "주종 선택값이 누락되었습니다.")
    List<Long> alcoholTypeIds
) {
}
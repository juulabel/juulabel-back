package com.juu.juulabel.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WriteDailyLifeRequest(
    @Schema(example = "이것은 제목입니다...")
    @NotNull(message = "제목이 누락되었습니다.")
    @Size(max = 30, message = "제목은 최대 30글자까지 가능합니다.")
    String title,

    @Schema(example = "이것은 내용입니다...")
    @NotNull(message = "내용이 누락되었습니다.")
    @Size(max = 1200, message = "내용은 최대 1200글자까지 가능합니다.")
    String content,

    @NotNull(message = "비밀글 여부가 누락되었습니다.")
    boolean isPrivate
) {
}

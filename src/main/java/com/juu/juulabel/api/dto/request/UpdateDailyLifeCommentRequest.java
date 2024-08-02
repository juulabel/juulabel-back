package com.juu.juulabel.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateDailyLifeCommentRequest(
    @Schema(example = "이것은 내용입니다...")
    @NotNull(message = "내용이 누락되었습니다.")
    @Size(max = 1200, message = "내용은 최대 1200글자까지 가능합니다.")
    String content
) {
}
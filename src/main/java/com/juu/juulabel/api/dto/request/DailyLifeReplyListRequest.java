package com.juu.juulabel.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DailyLifeReplyListRequest(
    Long lastReplyId,
    @Schema(example = "10")
    @NotNull(message = "페이지 사이즈가 누락되었습니다.")
    int pageSize
) {
}
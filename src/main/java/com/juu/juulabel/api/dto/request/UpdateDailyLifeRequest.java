package com.juu.juulabel.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateDailyLifeRequest(
    @Size(max = 30, message = "제목은 최대 30글자까지 가능합니다.")
    String title,

    @Size(max = 1200, message = "내용은 최대 1200글자까지 가능합니다.")
    String content,

    @NotNull(message = "비밀글 여부가 누락되었습니다.")
    boolean isPrivate
) {
}
package com.juu.juulabel.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MyAlcoholicDrinksListRequest(
    Long lastAlcoholicDrinksId,
    @Schema(example = "1")
    @NotNull(message = "페이지 사이즈가 누락되었습니다.")
    int pageSize
) {
}
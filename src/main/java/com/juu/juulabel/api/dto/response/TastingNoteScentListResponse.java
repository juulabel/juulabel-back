package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.CategoryWithScentSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "향 정보 조회")
public record TastingNoteScentListResponse(
        @Schema(description = "카테고리별 향 정보")
        List<CategoryWithScentSummary> categories
) {
}

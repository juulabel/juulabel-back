package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicBrewerySummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "양조장 상세 조회 응답")
public record BreweryDetailResponse(
        @Schema(description = "양조장 정보")
        BrewerySummary brewerySummary,
        @Schema(description = "전통주 리스트")
        List<AlcoholicBrewerySummary> alcoholicBrewerySummary
) {
}


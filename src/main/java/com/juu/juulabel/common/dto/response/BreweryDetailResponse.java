package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.response.AlcoholicBrewerySummary;
import com.juu.juulabel.alcohol.response.BrewerySummary;
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


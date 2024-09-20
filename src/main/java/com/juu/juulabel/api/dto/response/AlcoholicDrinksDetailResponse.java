package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.domain.dto.alcohol.TastingNoteSummary;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전통주 상세 조회 응답")
public record AlcoholicDrinksDetailResponse(
        @Schema(description = "전통주 상세 정보")
        AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo
) {
}

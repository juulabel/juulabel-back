package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.domain.dto.tastingnote.LikeTopTastingNoteSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSensorSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "전통주 상세 조회 응답")
public record AlcoholicDrinksDetailResponse(
        @Schema(description = "전통주 상세 정보")
        AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo,
        @Schema(description = "전통주 감각 정보")
        TastingNoteSensorSummary tastingNoteSensorSummary,
        @Schema(description = "좋아요 많은 시음노트")
        List<LikeTopTastingNoteSummary> tastingNoteSummary
) {
}

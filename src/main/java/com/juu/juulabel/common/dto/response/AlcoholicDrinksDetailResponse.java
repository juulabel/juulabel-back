package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.response.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.alcohol.response.IngredientSummary;
import com.juu.juulabel.alcohol.response.VolumePriceDetail;
import com.juu.juulabel.tastingnote.request.LikeTopTastingNoteSummary;
import com.juu.juulabel.tastingnote.request.TastingNoteSensorSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "전통주 상세 조회 응답")
public record AlcoholicDrinksDetailResponse(
        @Schema(description = "전통주 상세 정보")
        AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo,
        @Schema(description = "용량별 가격 정보")
        List<VolumePriceDetail> volumePriceDetails,
        @Schema(description = "전통주 원재료")
        List<IngredientSummary> ingredientSummary,
        @Schema(description = "전통주 감각 정보")
        TastingNoteSensorSummary tastingNoteSensorSummary,
        @Schema(description = "좋아요 많은 시음노트")
        List<LikeTopTastingNoteSummary> tastingNoteSummary
) {
}

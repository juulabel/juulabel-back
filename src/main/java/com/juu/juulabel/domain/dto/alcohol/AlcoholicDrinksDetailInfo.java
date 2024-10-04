package com.juu.juulabel.domain.dto.alcohol;


import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSensorSummary;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전통주 상세 정보")
public record AlcoholicDrinksDetailInfo(
        @Schema(description = "전통주 고유 번호")
        Long id,
        @Schema(description = "전통주명")
        String name,
        @Schema(description = "전통주 썸네일 URL")
        String thumbnail,
        @Schema(description = "전통주 도수")
        Double alcoholContent,
        @Schema(description = "전통주 용량")
        Double alcoholicVolume,
        @Schema(description = "전통주 가격")
        Double alcoholicPrice,
        @Schema(description = "전통주 원재료")
        String ingredients,
        @Schema(description = "주종 간단 정보")
        AlcoholTypeSummary alcoholType,
        @Schema(description = "양조장 간단 정보")
        BrewerySummary brewery
){

}

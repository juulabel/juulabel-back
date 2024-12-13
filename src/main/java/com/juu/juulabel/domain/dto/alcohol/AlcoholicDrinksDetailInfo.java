package com.juu.juulabel.domain.dto.alcohol;


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
        int alcoholicVolume,
        @Schema(description = "전통주 할인가")
        int discountPrice,
        @Schema(description = "전통주 정가")
        int regularPrice,
        @Schema(description = "평균 달점")
        Double rating,
        @Schema(description = "평균 시음노트 총 개수")
        int tastingNoteCount,
        @Schema(description = "주종 간단 정보")
        AlcoholTypeSummary alcoholType,
        @Schema(description = "양조장 간단 정보")
        BrewerySummary brewery
) {
}

package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "검색시 전통주 간단 정보")
public record AlcoholSearchSummary (
        @Schema(description = "전통주 고유 번호", example = "38")
        Long id,
        @Schema(description = "전통주명", example = "탁100")
        String name,
        @Schema(description = "전통주 썸네일 URL")
        String thumbnail,
//        @Schema(description = "전통주 도수")
//        Double alcoholContent,
        @Schema(description = "양조장 간단 정보")
        String breweryName
         ){
}

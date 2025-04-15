package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.common.annotation.SliceResponse;
import com.juu.juulabel.alcohol.request.AlcoholSearchSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

@SliceResponse(content = AlcoholSearchSummary.class)
@Schema(description = "주종별 전통주 리스트 조회 응답")
public record AlcoholicCategoryResponse (
    @Schema(description = "마지막 페이지 여부")
    boolean isLast,
    @Schema(description = "총 검색 결과 개수")
    long totalCount,
    @Schema(description = "전통주 간단 정보")
    Slice<AlcoholSearchSummary> alcoholicDrinks
){
}

package com.juu.juulabel.domain.dto.tastingnote;

import com.juu.juulabel.domain.dto.alcohol.FlavorDetail;
import com.juu.juulabel.domain.dto.alcohol.SensoryDetail;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TastingNoteSensorSummary(
        @Schema(description = "좋아요 제일 많이 받은 시음노트 id")
        Long tastingNoteId,
        @Schema(description = "전통주 색상")
        String rgb,
        @Schema(description = "전통주 향")
        List<String> scent,
//        @Schema(description = "전통주 맛")
//        List<String> flavor,
        @Schema(description = "전통주 맛")
        List<FlavorDetail> flavor,
        @Schema(description = "전통주 촉각")
        List<SensoryDetail> sensory
//        List<String> sensory

) {
}

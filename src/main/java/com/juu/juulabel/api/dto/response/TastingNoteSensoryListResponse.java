package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.SensoryLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "시각 및 촉각 정보 조회")
public record TastingNoteSensoryListResponse(
        @Schema(description = "감각 타입과 그에 대한 점수 및 설명 리스트")
        List<SensoryLevel> sensoryLevels
) {
}
package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "양조장 간단 정보")
public record BrewerySummary(
        @Schema(description = "양조장 고유 번호", example = "38")
        Long id,
        @Schema(description = "양조장 이름", example = "탁브루")
        String name,
        @Schema(description = "양조장 지역", example = "인천광역시 부평구")
        String region,
        @Schema(description = "양조장 대표님 한마디", example = "아직 말씀 하시기 전 이에요!")
        String message
) {
        public BrewerySummary(Long id, String name, String region, String message) {
                this.id = id;
                this.name = name;
                this.region = region;
                // message가 null인 경우 기본 값으로 설정
                this.message = (message != null && !message.isEmpty()) ? message : "아직 말씀 하시기 전 이에요!";
        }
}

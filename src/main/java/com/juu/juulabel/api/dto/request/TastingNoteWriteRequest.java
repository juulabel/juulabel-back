package com.juu.juulabel.api.dto.request;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetails;
import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@Schema(description = "시음노트 작성 요청")
public record TastingNoteWriteRequest(
        @Schema(description = "주종 고유 번호", example = "1")
        @NotNull(message = "주종 고유 번호가 누락되었습니다.")
        Long alcoholTypeId,
        @Schema(description = "전통주 고유 번호", example = "12", nullable = true)
        Long alcoholicDrinksId,
        @Schema(description = "색상 고유 번호", example = "7", nullable = true)
        Long colorId,
        @Schema(description = "전통주 세부 정보")
        @Valid
        @NotNull(message = "전통주 세부 정보가 누락되었습니다.")
        AlcoholicDrinksDetails alcoholicDrinksDetails,
        Map<SensoryType, String> sensoryMap
) {
}

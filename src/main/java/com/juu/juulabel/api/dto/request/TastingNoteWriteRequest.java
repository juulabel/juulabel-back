package com.juu.juulabel.api.dto.request;

import com.juu.juulabel.api.annotation.Rating;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetails;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorType;
import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
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

        @Schema(description = "향 고유 번호 리스트", example = "[1, 4, 7]", nullable = true)
        List<Long> scentIds,

        @Schema(description = "전통주 세부 정보")
        @Valid
        @NotNull(message = "전통주 세부 정보가 누락되었습니다.")
        AlcoholicDrinksDetails alcoholicDrinksDetails,

        @Schema(description = "촉각 정보(sensory : name)")
        Map<SensoryType, String> sensoryMap,

        @Schema(description = "미각 정보(flavor : name)")
        Map<FlavorType, String> flavorMap,

        @Schema(description = "부연 설명", example = "도수가 높은 것에 비해..")
        @Size(max = 1200, message = "주관 평가 부연 설명은 1200자를 초과할 수 없습니다.")
        String content,

        @Schema(description = "비공개 여부", example = "false")
        boolean isPrivate,

        @Schema(description = "달점", example = "4.5")
        @Rating
        Double rating
) {
}

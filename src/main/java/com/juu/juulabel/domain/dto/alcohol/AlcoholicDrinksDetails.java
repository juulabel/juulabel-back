package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "전통주 세부 정보")
public record AlcoholicDrinksDetails(
        @Schema(description = "주종명", example = "탁주")
        @NotNull(message = "주종이 누락되었습니다.")
        String alcoholTypeName,
        @Schema(description = "전통주명", example = "탁100")
        @NotNull(message = "전통주 이름이 누락되었습니다.")
        String alcoholicDrinksName,
        @Schema(description = "전통주 도수", example = "14.9")
        @NotNull(message = "전통주 도수가 누락되었습니다.")
        Double alcoholContent,
        @Schema(description = "양조장 이름", example = "양조장명")
        @NotNull(message = "양조장 이름이 누락되었습니다.")
        String breweryName,
        @Schema(description = "양조장 지역", example = "인천광역시 부평구")
        String breweryRegion
) {
}

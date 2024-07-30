package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

import java.util.List;

@Schema(description = "전통주 리스트 조회 응답")
public record AlcoholDrinksListResponse(
        boolean isLast,
        @Schema(description = "전통주 간단 정보")
        List<AlcoholicDrinksSummary> alcoholicDrinks
) {
        public static AlcoholDrinksListResponse fromSlice(Slice<AlcoholicDrinksSummary> alcoholicDrinks) {
                return new AlcoholDrinksListResponse(alcoholicDrinks.isLast(), alcoholicDrinks.getContent());
        }

}

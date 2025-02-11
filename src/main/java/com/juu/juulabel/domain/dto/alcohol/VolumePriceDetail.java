package com.juu.juulabel.domain.dto.alcohol;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전통주 용량별 가격 정보")
public record VolumePriceDetail (
    @Schema(description = "용량")
    int volume,
    @Schema(description = "전통주 할인가")
    int discountPrice,
    @Schema(description = "전통주 정가")
    int regularPrice
){ }

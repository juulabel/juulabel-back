package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.alcohol.UsedAlcoholTypeInfo;

import java.util.List;

public record AlcoholTypeListResponse(
    List<UsedAlcoholTypeInfo> alcoholTypeInfos
) {
}
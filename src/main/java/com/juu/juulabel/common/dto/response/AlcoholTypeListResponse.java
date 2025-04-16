package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.alcohol.request.UsedAlcoholTypeInfo;

import java.util.List;

public record AlcoholTypeListResponse(
    List<UsedAlcoholTypeInfo> alcoholTypeInfos
) {
}
package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.enums.member.Gender;

import java.util.List;

public record MyInfoResponse(
    String nickname,
    String email,
    boolean isNotificationsAllowed,
    String introduction,
    String profileImage,
    Gender gender,
    List<Long> alcoholTypeIds
) {
}
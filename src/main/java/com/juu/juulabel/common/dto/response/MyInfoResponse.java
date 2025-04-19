package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.member.domain.Gender;

import java.util.List;

public record MyInfoResponse(
    Long memberId,
    String nickname,
    String email,
    boolean hasBadge,
    boolean isNotificationsAllowed,
    String introduction,
    String profileImage,
    Gender gender,
    List<Long> alcoholTypeIds
) {
}
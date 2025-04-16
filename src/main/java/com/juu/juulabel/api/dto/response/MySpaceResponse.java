package com.juu.juulabel.api.dto.response;

public record MySpaceResponse(
        long memberId,
        String profileImage,
        String nickname,
        String introduction,
        boolean hasBadge,
        long myTastingNoteCount,
        long myDailyLifeCount,
        long followingCount,
        long followerCount,
        long savedTastingNoteCount
) {
}
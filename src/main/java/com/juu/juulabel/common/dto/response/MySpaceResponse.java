package com.juu.juulabel.common.dto.response;

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
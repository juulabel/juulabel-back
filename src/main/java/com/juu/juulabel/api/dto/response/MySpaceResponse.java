package com.juu.juulabel.api.dto.response;

public record MySpaceResponse(
        String profileImage,
        String nickname,
        String introduction,
        long myTastingNoteCount,
        long myDailyLifeCount,
        long savedTastingNoteCount
) {
}
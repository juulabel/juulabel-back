package com.juu.juulabel.tastingnote.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juu.juulabel.member.request.MemberInfo;

import java.time.LocalDateTime;

public record TastingNoteDetailInfo(
    Long tastingNoteId,
    MemberInfo memberInfo,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    LocalDateTime createdAt,

    String alcoholicDrinksName,
    String alcoholTypeName,
    Double alcoholContent,
    String breweryName,

    String rgbColor,
//    List<Long> sensoryLevelIds,
//    List<Long> scentIds,
//    List<Long> flavorLevelIds,

    String content,
    Double rating,
    long likeCount,
    long commentCount,
    boolean isLiked
) {
}

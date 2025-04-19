package com.juu.juulabel.tastingnote.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juu.juulabel.member.request.MemberInfo;

import java.time.LocalDateTime;

public record TastingNoteSummary(
    Long TastingNoteId,
    String alcoholicDrinksName,
    MemberInfo memberInfo,
    String thumbnailPath,
    String alcoholTypeName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    LocalDateTime createdAt,
    boolean hasMultipleImages
) {
}

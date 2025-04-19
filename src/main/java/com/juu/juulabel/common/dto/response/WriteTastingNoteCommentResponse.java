package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.member.request.MemberInfo;

public record WriteTastingNoteCommentResponse(
    String content,
    Long tastingNoteId,
    MemberInfo memberInfo
) {
}

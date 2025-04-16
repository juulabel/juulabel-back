package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.member.request.MemberInfo;

public record UpdateCommentResponse(
    String content,
    MemberInfo memberInfo
) {
}
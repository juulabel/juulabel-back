package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.member.MemberInfo;

public record UpdateDailyLifeCommentResponse(
    String content,
    MemberInfo memberInfo
) {
}
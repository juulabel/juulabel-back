package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.member.MemberInfo;

public record WriteDailyLifeCommentResponse(
    String content,
    Long dailyLifeId,
    MemberInfo memberInfo
) {
}

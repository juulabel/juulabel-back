package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.member.request.MemberInfo;

public record WriteDailyLifeCommentResponse(
    String content,
    Long dailyLifeId,
    MemberInfo memberInfo
) {
}

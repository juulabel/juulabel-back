package com.juu.juulabel.common.dto.request;


import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.domain.Provider;

import java.time.LocalDateTime;

public record MemberListRequest(
    String nickName,
    String email,
    LocalDateTime  createdAt,
//    LocalDateTime  deletedAt,
    MemberStatus status,
    Provider provider,
    boolean hasBadge,
    int pageSize
) {
}

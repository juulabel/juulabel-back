package com.juu.juulabel.admin.response;


import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.domain.Provider;

import java.time.LocalDateTime;

public record MemberListSummary(
        Long id,
        String nickName,
        String email,
        LocalDateTime createdAt,
        MemberStatus status,
        Provider provider,
        boolean hasBadge
) {
}

package com.juu.juulabel.domain.dto.member;

public record MemberInfo(
    Long memberId,
    String nickname,
    String profileImage
    // TODO : 뱃지 유무
) {
}

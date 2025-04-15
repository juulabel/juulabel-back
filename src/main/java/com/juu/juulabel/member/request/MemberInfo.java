package com.juu.juulabel.member.request;

public record MemberInfo(
    Long memberId,
    String nickname,
    String profileImage
    // TODO : 뱃지 유무
) {
}

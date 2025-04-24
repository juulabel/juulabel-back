package com.juu.juulabel.admin;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.notification.domain.NotificationType;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberRole;
import com.juu.juulabel.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberReader memberReader;
    private final NotificationService notificationService;

    @Transactional
    public void assignBadge(String email, Member loginMember) {
        if (loginMember.getRole() != MemberRole.ROLE_ADMIN) {
            throw new BaseException(ErrorCode.ADMIN_NOT_FOUND);
        }
        Member member = memberReader.getByEmail(email); // 닉네임은 변경 가능하기 때문에 이메일로만 검증
        member.assignBadge();

        notificationService.send(
            loginMember,
            NotificationType.ADMIN_NOTIFY,
            "신청하신 소믈리에 뱃지가 승인되었어요.",
            "v1/api/members/my-space",
            "https://d2e2lktek3envx.cloudfront.net/juulabel-logo.png"
        );
    }
}

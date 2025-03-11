package com.juu.juulabel.api.service.admin;

import com.juu.juulabel.api.service.notification.NotificationService;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.enums.member.MemberRole;
import com.juu.juulabel.domain.enums.notification.NotificationType;
import com.juu.juulabel.domain.repository.reader.MemberReader;
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
            throw new BaseException(ErrorCode.NOT_FOUND_ADMIN);
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

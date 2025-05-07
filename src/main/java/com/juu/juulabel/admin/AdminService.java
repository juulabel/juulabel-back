package com.juu.juulabel.admin;

import com.juu.juulabel.admin.repository.AdminReader;
import com.juu.juulabel.admin.repository.query.AdminQueryRepository;
import com.juu.juulabel.admin.response.MemberListSummary;
import com.juu.juulabel.common.dto.request.MemberListRequest;
import com.juu.juulabel.common.dto.response.MemberListResponse;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.notification.domain.NotificationType;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberRole;
import com.juu.juulabel.notification.service.NotificationService;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberReader memberReader;
    private final AdminReader adminReader;
    private final NotificationService notificationService;
    private final AdminQueryRepository adminQueryRepository;

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

    @Transactional(readOnly = true)
    public MemberListResponse loadMemberList(Member loginMember, MemberListRequest request) {
        final Slice<MemberListSummary> memberList =
                adminQueryRepository.getMemberList(loginMember,request,request.pageSize());
        return new MemberListResponse(memberList);
    }
}

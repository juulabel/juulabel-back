package com.juu.juulabel.admin.repository;

import com.juu.juulabel.admin.repository.query.AdminQueryRepository;
import com.juu.juulabel.admin.response.MemberListSummary;
import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.common.dto.request.MemberListRequest;
import com.juu.juulabel.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class AdminReader {
    private final AdminQueryRepository adminQueryRepository;

    public Slice<MemberListSummary> getMemberList(Member loginMember, MemberListRequest request, int pagesize){
        return adminQueryRepository.getMemberList(loginMember,request,pagesize);
    }

}

package com.juu.juulabel.report.processor;

import com.juu.juulabel.member.service.MemberService;
import com.juu.juulabel.report.ReportCreateRequest;
import com.juu.juulabel.report.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReportProcessor implements ReportProcessor {

    private final MemberService memberService;

    @Override
    public ReportType getReportType() {
        return ReportType.MEMBER;
    }

    @Override
    public void process(ReportCreateRequest request) {
        memberService.findById(request.reportedContentId());
    }
}

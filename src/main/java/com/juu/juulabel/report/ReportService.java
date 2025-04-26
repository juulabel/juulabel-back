package com.juu.juulabel.report;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberService MemberService;

    @Transactional
    public void createReport(Long reporterId, ReportCreateRequest request) {
        Member reporter = MemberService.findById(reporterId);
        Member reportedUser = MemberService.findById(request.reportedUserId());

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(request.reason())
                .type(request.type())
                .status(ReportStatus.PENDING)
                .build();

        reportRepository.save(report);
    }
}

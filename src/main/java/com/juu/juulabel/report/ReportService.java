package com.juu.juulabel.report;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.service.MemberService;
import com.juu.juulabel.report.processor.ReportProcessor;
import com.juu.juulabel.report.processor.ReportProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberService MemberService;
    private final ReportProcessorFactory reportProcessorFactory;

    @Transactional
    public void createReport(long reporterId, ReportCreateRequest request) {
        Member reporter = MemberService.findById(reporterId);

        ReportProcessor processor = reportProcessorFactory.getProcessor(request.type());
        processor.process(request);

        Report report = Report.builder()
                .reporter(reporter)
                .reportedContentId(request.reportedContentId())
                .reason(request.reason())
                .type(request.type())
                .status(ReportStatus.PENDING)
                .build();

        reportRepository.save(report);
    }
}

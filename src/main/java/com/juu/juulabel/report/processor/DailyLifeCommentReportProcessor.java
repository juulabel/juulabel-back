package com.juu.juulabel.report.processor;

import com.juu.juulabel.dailylife.service.DailyLifeCommentService;
import com.juu.juulabel.report.ReportCreateRequest;
import com.juu.juulabel.report.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyLifeCommentReportProcessor implements ReportProcessor {

    private final DailyLifeCommentService dailyLifeCommentService;

    @Override
    public ReportType getReportType() {
        return ReportType.DAILY_LIFE_COMMENT;
    }

    @Override
    public void process(ReportCreateRequest request) {
        dailyLifeCommentService.findById(request.reportedContentId());
    }
}

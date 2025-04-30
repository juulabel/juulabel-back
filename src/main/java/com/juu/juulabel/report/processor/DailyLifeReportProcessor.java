package com.juu.juulabel.report.processor;

import com.juu.juulabel.dailylife.service.DailyLifeService;
import com.juu.juulabel.report.ReportCreateRequest;
import com.juu.juulabel.report.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyLifeReportProcessor implements ReportProcessor {

    private final DailyLifeService dailyLifeService;

    @Override
    public ReportType getReportType() {
        return ReportType.DAILY_LIFE;
    }

    @Override
    public void process(ReportCreateRequest request) {
        dailyLifeService.findById(request.reportedContentId());
    }
}

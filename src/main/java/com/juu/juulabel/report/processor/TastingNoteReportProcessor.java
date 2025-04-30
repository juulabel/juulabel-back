package com.juu.juulabel.report.processor;

import com.juu.juulabel.report.ReportCreateRequest;
import com.juu.juulabel.report.ReportType;
import com.juu.juulabel.tastingnote.service.TastingNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TastingNoteReportProcessor implements ReportProcessor {

    private final TastingNoteService tastingNoteService;

    @Override
    public ReportType getReportType() {
        return ReportType.TASTING_NOTE;
    }

    @Override
    public void process(ReportCreateRequest request) {
        tastingNoteService.findById(request.reportedContentId());
    }
}

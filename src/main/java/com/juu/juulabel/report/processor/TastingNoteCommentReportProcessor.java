package com.juu.juulabel.report.processor;

import com.juu.juulabel.report.ReportCreateRequest;
import com.juu.juulabel.report.ReportType;
import com.juu.juulabel.tastingnote.service.TastingNoteCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TastingNoteCommentReportProcessor implements ReportProcessor {

    private final TastingNoteCommentService tastingNoteCommentService;

    @Override
    public ReportType getReportType() {
        return ReportType.TASTING_NOTE_COMMENT;
    }

    @Override
    public void process(ReportCreateRequest request) {
        tastingNoteCommentService.findById(request.reportedContentId());
    }
}

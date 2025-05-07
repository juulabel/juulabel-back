package com.juu.juulabel.report.processor;

import com.juu.juulabel.report.ReportCreateRequest;
import com.juu.juulabel.report.ReportType;

public interface ReportProcessor {
    ReportType getReportType();

    void process(ReportCreateRequest request);
}

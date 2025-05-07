package com.juu.juulabel.report.processor;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.report.ReportType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ReportProcessorFactory {

    private final Map<ReportType, ReportProcessor> processorMap;

    public ReportProcessorFactory(List<ReportProcessor> reportProcessors) {
        this.processorMap = reportProcessors.stream()
                .collect(Collectors.toMap(ReportProcessor::getReportType, Function.identity()));
    }
    public ReportProcessor getProcessor(ReportType type) {
        ReportProcessor processor = processorMap.get(type);
        if (processor == null) {
            throw new BaseException(ErrorCode.REPORT_PROCESSOR_NOT_FOUND);
        }
        return processor;
    }
}

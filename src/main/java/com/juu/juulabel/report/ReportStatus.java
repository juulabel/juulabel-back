package com.juu.juulabel.report;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportStatus {
    PENDING("대기중"),
    REVIEWING("검토중"),
    REJECTED("거절"),
    APPROVED("승인");

    private final String description;
}
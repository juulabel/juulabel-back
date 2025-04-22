package com.juu.juulabel.report;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportType {
    USER("유저"),
    TASTING_NOTE("시음노트"),
    DAILY_LIFE("일상생활"),
    COMMENT("댓글");

    private final String description;
}
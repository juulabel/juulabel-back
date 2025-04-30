package com.juu.juulabel.report;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportType {
    MEMBER("멤버"),
    TASTING_NOTE("시음노트"),
    TASTING_NOTE_COMMENT("시음노트 댓글"),
    DAILY_LIFE("일상생활"),
    DAILY_LIFE_COMMENT("일상생활 댓글");

    private final String description;
}
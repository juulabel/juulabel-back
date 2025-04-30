package com.juu.juulabel.report;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportCreateRequest(
        @NotNull(message = "신고할 컨텐츠를 넣어주세요")
        @Min(value = 1, message = "ID는 1 이상이어야 합니다.")
        Long reportedContentId,

        @NotBlank(message = "신고 사유를 넣어주세요.")
        String reason,

        ReportType type
) {
}

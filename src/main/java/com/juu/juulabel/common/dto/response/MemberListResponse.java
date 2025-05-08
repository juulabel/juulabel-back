package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.admin.response.MemberListSummary;
import org.springframework.data.domain.Slice;

public record MemberListResponse(
        Slice<MemberListSummary> memberLifeSummaries
) {
}

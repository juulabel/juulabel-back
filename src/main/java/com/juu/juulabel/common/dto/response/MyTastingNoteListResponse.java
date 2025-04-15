package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.tastingnote.request.MyTastingNoteSummary;
import org.springframework.data.domain.Slice;

public record MyTastingNoteListResponse(
    Slice<MyTastingNoteSummary> myTastingNoteSummaries
) {
}

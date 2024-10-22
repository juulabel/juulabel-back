package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.tastingnote.MyTastingNoteSummary;
import org.springframework.data.domain.Slice;

public record MyTastingNoteListResponse(
    Slice<MyTastingNoteSummary> myTastingNoteSummaries
) {
}

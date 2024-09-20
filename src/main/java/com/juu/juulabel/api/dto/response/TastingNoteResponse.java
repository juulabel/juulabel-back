package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.ImageInfo;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteDetailInfo;

import java.util.List;

public record TastingNoteResponse(
    TastingNoteDetailInfo tastingNoteDetailInfo,
    List<Long> sensoryLevelIds,
    List<Long> scentIds,
    List<Long> flavorLevelIds,
    ImageInfo imageInfo
) {
}

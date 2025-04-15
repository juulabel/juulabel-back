package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.tastingnote.domain.TastingNoteImage;
import com.juu.juulabel.alcohol.repository.query.TastingNoteImageQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class TastingNoteImageReader {

    private final TastingNoteImageQueryRepository tastingNoteImageQueryRepository;

    public List<String> getImageUrlList(Long tastingNoteId) {
        return tastingNoteImageQueryRepository.getImageUrlList(tastingNoteId);
    }

    public List<TastingNoteImage> getImageList(Long tastingNoteId) {
        return tastingNoteImageQueryRepository.getImageList(tastingNoteId);
    }
}

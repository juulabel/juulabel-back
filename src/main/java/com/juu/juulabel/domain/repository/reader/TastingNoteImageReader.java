package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteImage;
import com.juu.juulabel.domain.repository.query.TastingNoteImageQueryRepository;
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

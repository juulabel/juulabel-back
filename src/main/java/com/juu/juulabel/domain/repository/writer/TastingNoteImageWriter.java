package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.alcohol.TastingNote;
import com.juu.juulabel.domain.entity.alcohol.TastingNoteImage;
import com.juu.juulabel.domain.repository.jpa.TastingNoteImageJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class TastingNoteImageWriter {

    private final TastingNoteImageJpaRepository tastingNoteImageJpaRepository;

    public TastingNoteImage store(TastingNote tastingNote, int seq, String imagePath) {
        TastingNoteImage tastingNoteImage = TastingNoteImage.create(tastingNote, seq, imagePath);
        return tastingNoteImageJpaRepository.save(tastingNoteImage);
    }

}

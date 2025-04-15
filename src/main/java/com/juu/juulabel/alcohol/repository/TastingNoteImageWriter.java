package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.tastingnote.domain.TastingNote;
import com.juu.juulabel.tastingnote.domain.TastingNoteImage;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteImageJpaRepository;
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

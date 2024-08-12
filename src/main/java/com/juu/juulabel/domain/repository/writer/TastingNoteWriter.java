package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.alcohol.TastingNote;
import com.juu.juulabel.domain.entity.alcohol.TastingNoteScent;
import com.juu.juulabel.domain.repository.jpa.TastingNoteRepository;
import com.juu.juulabel.domain.repository.jpa.TastingNoteScentJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Writer
@RequiredArgsConstructor
public class TastingNoteWriter {

    private final TastingNoteRepository tastingNoteRepository;
    private final TastingNoteScentJpaRepository tastingNoteScentJpaRepository;

    public TastingNote create(TastingNote tastingNote, List<TastingNoteScent> tastingNoteScents) {
        final TastingNote result = tastingNoteRepository.save(tastingNote);
        tastingNoteScentJpaRepository.saveAll(tastingNoteScents);
        return result;
    }

}

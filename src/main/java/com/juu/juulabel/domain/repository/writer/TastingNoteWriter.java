package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.alcohol.TastingNote;
import com.juu.juulabel.domain.entity.alcohol.TastingNoteFlavorLevel;
import com.juu.juulabel.domain.entity.alcohol.TastingNoteScent;
import com.juu.juulabel.domain.entity.alcohol.TastingNoteSensoryLevel;
import com.juu.juulabel.domain.repository.jpa.TastingNoteFlavorLevelJpaRepository;
import com.juu.juulabel.domain.repository.jpa.TastingNoteRepository;
import com.juu.juulabel.domain.repository.jpa.TastingNoteScentJpaRepository;
import com.juu.juulabel.domain.repository.jpa.TastingNoteSensoryLevelJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Writer
@RequiredArgsConstructor
public class TastingNoteWriter {

    private final TastingNoteRepository tastingNoteRepository;
    private final TastingNoteScentJpaRepository tastingNoteScentJpaRepository;
    private final TastingNoteFlavorLevelJpaRepository tastingNoteFlavorLevelJpaRepository;
    private final TastingNoteSensoryLevelJpaRepository tastingNoteSensoryLevelJpaRepository;

    public TastingNote create(TastingNote tastingNote,
                              List<TastingNoteScent> tastingNoteScents,
                              List<TastingNoteFlavorLevel> tastingNoteFlavorLevels,
                              List<TastingNoteSensoryLevel> tastingNoteSensoryLevels) {
        final TastingNote result = tastingNoteRepository.save(tastingNote);
        tastingNoteScentJpaRepository.saveAll(tastingNoteScents);
        tastingNoteFlavorLevelJpaRepository.saveAll(tastingNoteFlavorLevels);
        tastingNoteSensoryLevelJpaRepository.saveAll(tastingNoteSensoryLevels);
        return result;
    }

}

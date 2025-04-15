package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.tastingnote.domain.TastingNote;
import com.juu.juulabel.tastingnote.domain.TastingNoteFlavorLevel;
import com.juu.juulabel.tastingnote.domain.TastingNoteScent;
import com.juu.juulabel.tastingnote.domain.TastingNoteSensoryLevel;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteFlavorLevelJpaRepository;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteRepository;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteScentJpaRepository;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteSensoryLevelJpaRepository;
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

    public void update(Long tastingNoteId,
                       List<TastingNoteScent> tastingNoteScents,
                       List<TastingNoteFlavorLevel> tastingNoteFlavorLevels,
                       List<TastingNoteSensoryLevel> tastingNoteSensoryLevels
    ) {
        tastingNoteScentJpaRepository.deleteByTastingNoteId(tastingNoteId);
        tastingNoteFlavorLevelJpaRepository.deleteByTastingNoteId(tastingNoteId);
        tastingNoteSensoryLevelJpaRepository.deleteByTastingNoteId(tastingNoteId);

        tastingNoteScentJpaRepository.saveAll(tastingNoteScents);
        tastingNoteFlavorLevelJpaRepository.saveAll(tastingNoteFlavorLevels);
        tastingNoteSensoryLevelJpaRepository.saveAll(tastingNoteSensoryLevels);
    }
}

package com.juu.juulabel.tastingnote.repository.jpa;

import com.juu.juulabel.tastingnote.domain.TastingNoteFlavorLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteFlavorLevelJpaRepository extends JpaRepository<TastingNoteFlavorLevel, Long> {
    void deleteByTastingNoteId(Long tastingNoteId);
}

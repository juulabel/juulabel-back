package com.juu.juulabel.tastingnote.repository.jpa;

import com.juu.juulabel.tastingnote.domain.TastingNoteSensoryLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteSensoryLevelJpaRepository extends JpaRepository<TastingNoteSensoryLevel, Long> {
    void deleteByTastingNoteId(Long tastingNoteId);
}

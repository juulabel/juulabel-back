package com.juu.juulabel.tastingnote.repository.jpa;

import com.juu.juulabel.tastingnote.domain.TastingNoteScent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteScentJpaRepository extends JpaRepository<TastingNoteScent, Long> {
    void deleteByTastingNoteId(Long tastingNoteId);
}

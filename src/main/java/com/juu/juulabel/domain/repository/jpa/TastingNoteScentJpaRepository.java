package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.tastingnote.TastingNoteScent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteScentJpaRepository extends JpaRepository<TastingNoteScent, Long> {
    void deleteByTastingNoteId(Long tastingNoteId);
}

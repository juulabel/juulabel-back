package com.juu.juulabel.tastingnote.repository.jpa;

import com.juu.juulabel.tastingnote.domain.TastingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long> {
}

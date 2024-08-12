package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.TastingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long> {
}

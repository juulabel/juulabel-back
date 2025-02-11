package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.tastingnote.TastingNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TastingNoteJpaRepository extends JpaRepository<TastingNote, Long> {
    Optional<TastingNote> findByIdAndDeletedAtIsNull(Long id);
}

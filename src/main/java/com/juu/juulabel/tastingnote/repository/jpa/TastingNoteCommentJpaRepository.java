package com.juu.juulabel.tastingnote.repository.jpa;

import com.juu.juulabel.tastingnote.domain.TastingNoteComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TastingNoteCommentJpaRepository extends JpaRepository<TastingNoteComment, Long> {
    Optional<TastingNoteComment> findByIdAndDeletedAtIsNull(Long id);
}

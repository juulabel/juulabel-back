package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.tastingnote.TastingNoteComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TastingNoteCommentJpaRepository extends JpaRepository<TastingNoteComment, Long> {
    Optional<TastingNoteComment> findByIdAndDeletedAtIsNull(Long id);
}

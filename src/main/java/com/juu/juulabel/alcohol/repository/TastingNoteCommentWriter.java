package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.tastingnote.domain.TastingNoteComment;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteCommentJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class TastingNoteCommentWriter {

    private final TastingNoteCommentJpaRepository tastingNoteCommentJpaRepository;

    public TastingNoteComment store(TastingNoteComment comment) {
        return tastingNoteCommentJpaRepository.save(comment);
    }
}

package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteComment;
import com.juu.juulabel.domain.repository.jpa.TastingNoteCommentJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class TastingNoteCommentWriter {

    private final TastingNoteCommentJpaRepository tastingNoteCommentJpaRepository;

    public TastingNoteComment store(TastingNoteComment comment) {
        return tastingNoteCommentJpaRepository.save(comment);
    }
}

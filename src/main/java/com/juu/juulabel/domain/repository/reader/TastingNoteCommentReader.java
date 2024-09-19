package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteComment;
import com.juu.juulabel.domain.repository.jpa.TastingNoteCommentJpaRepository;
import com.juu.juulabel.domain.repository.query.TastingNoteCommentQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class TastingNoteCommentReader {

    private final TastingNoteCommentJpaRepository tastingNoteCommentJpaRepository;
    private final TastingNoteCommentQueryRepository tastingNoteCommentQueryRepository;

    public TastingNoteComment getById(Long tastingNoteCommentId) {
        return tastingNoteCommentJpaRepository.findByIdAndDeletedAtIsNull(tastingNoteCommentId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_COMMENT));
    }

}

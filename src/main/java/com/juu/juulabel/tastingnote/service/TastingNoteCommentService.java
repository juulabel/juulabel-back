package com.juu.juulabel.tastingnote.service;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.tastingnote.domain.TastingNoteComment;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteCommentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TastingNoteCommentService {
    private final TastingNoteCommentJpaRepository tastingNoteCommentJpaRepository;

    public TastingNoteComment findById(long id) {
        return tastingNoteCommentJpaRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.TASTING_NOTE_COMMENT_NOT_FOUND));
    }
}

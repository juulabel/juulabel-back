package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.comment.CommentSummary;
import com.juu.juulabel.domain.dto.comment.ReplySummary;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteComment;
import com.juu.juulabel.domain.repository.jpa.TastingNoteCommentJpaRepository;
import com.juu.juulabel.domain.repository.query.TastingNoteCommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class TastingNoteCommentReader {

    private final TastingNoteCommentJpaRepository tastingNoteCommentJpaRepository;
    private final TastingNoteCommentQueryRepository tastingNoteCommentQueryRepository;

    public TastingNoteComment getById(Long tastingNoteCommentId) {
        return tastingNoteCommentJpaRepository.findByIdAndDeletedAtIsNull(tastingNoteCommentId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_COMMENT));
    }

    public Slice<CommentSummary> getAllByTastingNoteId(
        Member member,
        Long tastingNoteId,
        Long lastCommentId,
        int pageSize
    ) {
        return tastingNoteCommentQueryRepository.getAllByTastingNoteId(member, tastingNoteId, lastCommentId, pageSize);
    }

    public Slice<ReplySummary> getAllRepliesByParentId(
        Member member,
        Long tastingNoteId,
        Long tastingNoteCommentId,
        Long lastReplyId,
        int pageSize
    ) {
        return tastingNoteCommentQueryRepository.getAllRepliesByParentId(
            member,
            tastingNoteId,
            tastingNoteCommentId,
            lastReplyId,
            pageSize
        );
    }
}

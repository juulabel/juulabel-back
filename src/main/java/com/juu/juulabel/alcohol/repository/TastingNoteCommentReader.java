package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.common.dto.comment.CommentSummary;
import com.juu.juulabel.common.dto.comment.ReplySummary;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.tastingnote.domain.TastingNoteComment;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteCommentJpaRepository;
import com.juu.juulabel.alcohol.repository.query.TastingNoteCommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class TastingNoteCommentReader {

    private final TastingNoteCommentJpaRepository tastingNoteCommentJpaRepository;
    private final TastingNoteCommentQueryRepository tastingNoteCommentQueryRepository;

    public TastingNoteComment getById(Long tastingNoteCommentId) {
        return tastingNoteCommentJpaRepository.findByIdAndDeletedAtIsNull(tastingNoteCommentId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.COMMENT_NOT_FOUND));
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

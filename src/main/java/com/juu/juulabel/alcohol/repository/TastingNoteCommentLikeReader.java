package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.tastingnote.domain.TastingNoteComment;
import com.juu.juulabel.tastingnote.domain.TastingNoteCommentLike;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteCommentLikeJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class TastingNoteCommentLikeReader {

    private final TastingNoteCommentLikeJpaRepository tastingNoteCommentLikeJpaRepository;

    public Optional<TastingNoteCommentLike> findByMemberAndTastingNoteComment(Member member, TastingNoteComment comment) {
        return tastingNoteCommentLikeJpaRepository.findByMemberAndTastingNoteComment(member, comment);
    }
}

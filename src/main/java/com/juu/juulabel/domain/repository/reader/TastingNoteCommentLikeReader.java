package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteComment;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteCommentLike;
import com.juu.juulabel.domain.repository.jpa.TastingNoteCommentLikeJpaRepository;
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

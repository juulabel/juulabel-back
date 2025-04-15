package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.tastingnote.domain.TastingNoteComment;
import com.juu.juulabel.tastingnote.domain.TastingNoteCommentLike;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteCommentLikeJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class TastingNoteCommentLikeWriter {

    private final TastingNoteCommentLikeJpaRepository tastingNoteCommentLikeJpaRepository;

    public void delete(TastingNoteCommentLike tastingNoteCommentLike) {
        tastingNoteCommentLikeJpaRepository.delete(tastingNoteCommentLike);
    }

    public void store(Member member, TastingNoteComment tastingNoteComment) {
        TastingNoteCommentLike tastingNoteCommentLike = TastingNoteCommentLike.create(member, tastingNoteComment);
        tastingNoteCommentLikeJpaRepository.save(tastingNoteCommentLike);
    }
}

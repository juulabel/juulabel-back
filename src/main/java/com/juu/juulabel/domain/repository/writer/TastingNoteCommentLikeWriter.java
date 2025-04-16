package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteComment;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteCommentLike;
import com.juu.juulabel.domain.repository.jpa.TastingNoteCommentLikeJpaRepository;
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

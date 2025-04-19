package com.juu.juulabel.tastingnote.repository.jpa;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.tastingnote.domain.TastingNoteComment;
import com.juu.juulabel.tastingnote.domain.TastingNoteCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TastingNoteCommentLikeJpaRepository extends JpaRepository<TastingNoteCommentLike, Long> {
    Optional<TastingNoteCommentLike> findByMemberAndTastingNoteComment(Member member, TastingNoteComment comment);
}

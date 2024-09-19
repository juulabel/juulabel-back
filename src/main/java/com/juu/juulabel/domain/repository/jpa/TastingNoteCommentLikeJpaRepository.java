package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteComment;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TastingNoteCommentLikeJpaRepository extends JpaRepository<TastingNoteCommentLike, Long> {
    Optional<TastingNoteCommentLike> findByMemberAndTastingNoteComment(Member member, TastingNoteComment comment);
}

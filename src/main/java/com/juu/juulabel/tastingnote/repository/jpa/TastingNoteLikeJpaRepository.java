package com.juu.juulabel.tastingnote.repository.jpa;

import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.tastingnote.domain.TastingNote;
import com.juu.juulabel.tastingnote.domain.TastingNoteLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TastingNoteLikeJpaRepository extends JpaRepository<TastingNoteLike, Long> {
    Optional<TastingNoteLike> findByMemberAndTastingNote(Member member, TastingNote tastingNote);
}

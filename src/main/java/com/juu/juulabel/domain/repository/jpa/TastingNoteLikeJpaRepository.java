package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNote;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TastingNoteLikeJpaRepository extends JpaRepository<TastingNoteLike, Long> {
    Optional<TastingNoteLike> findByMemberAndTastingNote(Member member, TastingNote tastingNote);
}

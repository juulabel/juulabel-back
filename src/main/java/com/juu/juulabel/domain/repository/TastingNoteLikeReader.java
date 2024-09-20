package com.juu.juulabel.domain.repository;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNote;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteLike;
import com.juu.juulabel.domain.repository.jpa.TastingNoteLikeJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class TastingNoteLikeReader {

    private final TastingNoteLikeJpaRepository tastingNoteLikeJpaRepository;

    public Optional<TastingNoteLike> findByMemberAndTastingNote(Member member, TastingNote tastingNote) {
        return tastingNoteLikeJpaRepository.findByMemberAndTastingNote(member, tastingNote);
    }
}

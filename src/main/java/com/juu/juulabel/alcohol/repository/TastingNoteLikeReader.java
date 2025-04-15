package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.tastingnote.domain.TastingNote;
import com.juu.juulabel.tastingnote.domain.TastingNoteLike;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteLikeJpaRepository;
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

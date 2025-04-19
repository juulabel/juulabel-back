package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.tastingnote.domain.TastingNote;
import com.juu.juulabel.tastingnote.domain.TastingNoteLike;
import com.juu.juulabel.tastingnote.repository.jpa.TastingNoteLikeJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class TastingNoteLikeWriter {

    private final TastingNoteLikeJpaRepository tastingNoteLikeJpaRepository;

    public void store(Member member, TastingNote tastingNote) {
        TastingNoteLike tastingNoteLike = TastingNoteLike.create(member, tastingNote);
        tastingNoteLikeJpaRepository.save(tastingNoteLike);
    }

    public void delete(TastingNoteLike tastingNote) {
        tastingNoteLikeJpaRepository.delete(tastingNote);
    }

}

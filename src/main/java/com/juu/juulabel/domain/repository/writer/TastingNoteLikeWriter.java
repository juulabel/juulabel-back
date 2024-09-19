package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNote;
import com.juu.juulabel.domain.entity.tastingnote.TastingNoteLike;
import com.juu.juulabel.domain.repository.jpa.TastingNoteLikeJpaRepository;
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

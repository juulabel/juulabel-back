package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.tastingnote.MyTastingNoteSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteDetailInfo;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSummary;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.TastingNote;
import com.juu.juulabel.domain.repository.jpa.TastingNoteJpaRepository;
import com.juu.juulabel.domain.repository.query.TastingNoteQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class TastingNoteReader {

    private final TastingNoteQueryRepository tastingNoteQueryRepository;
    private final TastingNoteJpaRepository tastingNoteJpaRepository;

    public Slice<AlcoholicDrinksSummary> getAllAlcoholicDrinks(final String search,
                                                               final String lastAlcoholicDrinksName,
                                                               final int pageSize) {
        return tastingNoteQueryRepository.findAllAlcoholicDrinks(search, lastAlcoholicDrinksName, pageSize);
    }

    public Slice<TastingNoteSummary> getAllTastingNotes(Member member, Long lastTastingNoteId, int pageSize) {
        return tastingNoteQueryRepository.getAllTastingNotes(member, lastTastingNoteId, pageSize);
    }

    public TastingNoteDetailInfo getTastingNoteDetailById(Long tastingNoteId, Member member) {
        return tastingNoteQueryRepository.getTastingNoteDetailById(tastingNoteId, member);
    }

    public List<Long> getSensoryLevelIds(Long tastingNoteId) {
        return tastingNoteQueryRepository.getSensoryLevelIds(tastingNoteId);
    }

    public List<Long> getScentIds(Long tastingNoteId) {
        return tastingNoteQueryRepository.getScentIds(tastingNoteId);
    }

    public List<Long> getFlavorLevelIds(Long tastingNoteId) {
        return tastingNoteQueryRepository.getFlavorLevelIds(tastingNoteId);
    }

    public TastingNote getById(Long tastingNoteId) {
        return tastingNoteJpaRepository.findByIdAndDeletedAtIsNull(tastingNoteId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TASTING_NOTE));
    }

    public long getMyTastingNoteCount(Member member) {
        return tastingNoteQueryRepository.getMyTastingNoteCount(member);
    }

    public long getTastingNoteCountByMemberId(Long memberId) {
        return tastingNoteQueryRepository.getTastingNoteCountByMemberId(memberId);
    }

    public Slice<MyTastingNoteSummary> getAllMyTastingNotes(Member member, Long lastTastingNoteId, int pageSize) {
        return tastingNoteQueryRepository.getAllMyTastingNotes(member, lastTastingNoteId, pageSize);
    }

    public Slice<TastingNoteSummary> getAllTastingNotesByMember(Long memberId, Long lastTastingNoteId, int pageSize) {
        return tastingNoteQueryRepository.getAllTastingNotesByMember(memberId, lastTastingNoteId, pageSize);
    }
}

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

    public long countBySearch(final String search) {
        return tastingNoteQueryRepository.countBySearch(search);
    }

    public Slice<TastingNoteSummary> getAllTastingNotes(Member member, Long lastTastingNoteId, int pageSize) {
        return tastingNoteQueryRepository.getAllTastingNotes(member, lastTastingNoteId, pageSize);
    }

    public TastingNoteDetailInfo getTastingNoteDetailById(Long tastingNoteId, Member member) {
        return tastingNoteQueryRepository.getTastingNoteDetailById(tastingNoteId, member);
    }

    public List<Long> getSensoryLevelIds(Long tastingNoteId, Member member) {
        return tastingNoteQueryRepository.getSensoryLevelIds(tastingNoteId, member);
    }

    public List<Long> getScentIds(Long tastingNoteId, Member member) {
        return tastingNoteQueryRepository.getScentIds(tastingNoteId, member);
    }

    public List<Long> getFlavorLevelIds(Long tastingNoteId, Member member) {
        return tastingNoteQueryRepository.getFlavorLevelIds(tastingNoteId, member);
    }

    public TastingNote getById(Long tastingNoteId) {
        return tastingNoteJpaRepository.findByIdAndDeletedAtIsNull(tastingNoteId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TASTING_NOTE));
    }

    public long getMyTastingNoteCount(Member member) {
        return tastingNoteQueryRepository.getMyTastingNoteCount(member);
    }

    public long getTastingNoteCountByMemberId(Long memberId, Member loginMember) {
        return tastingNoteQueryRepository.getTastingNoteCountByMemberId(memberId, loginMember);
    }

    public Slice<MyTastingNoteSummary> getAllMyTastingNotes(Member member, Long lastTastingNoteId, int pageSize) {
        return tastingNoteQueryRepository.getAllMyTastingNotes(member, lastTastingNoteId, pageSize);
    }

    public Slice<TastingNoteSummary> getAllTastingNotesByMember(Member loginMember, Long memberId, Long lastTastingNoteId, int pageSize) {
        return tastingNoteQueryRepository.getAllTastingNotesByMember(loginMember, memberId, lastTastingNoteId, pageSize);
    }

    public Long getAlcoholicDrinksByTastingNoteId(Long tastingNoteId) {
        return tastingNoteQueryRepository.getAlcoholicDrinksByTastingNoteId(tastingNoteId);
    }
}

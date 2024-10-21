package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeSummary;
import com.juu.juulabel.domain.dto.dailylife.MyDailyLifeSummary;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeJpaRepository;
import com.juu.juulabel.domain.repository.query.DailyLifeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class DailyLifeReader {

    private final DailyLifeQueryRepository dailyLifeQueryRepository;
    private final DailyLifeJpaRepository dailyLifeJpaRepository;

    public DailyLifeDetailInfo getDailyLifeDetailById(final Long dailyLifeId, final Member member) {
        return dailyLifeQueryRepository.getDailyLifeDetailById(dailyLifeId, member);
    }

    public DailyLife getById(final Long dailyLifeId) {
        return dailyLifeJpaRepository.findByIdAndDeletedAtIsNull(dailyLifeId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_DAILY_LIFE));
    }

    public Slice<DailyLifeSummary> getAllDailyLives(final Member member,
                                                    final Long lastDailyLifeId,
                                                    final int pageSize) {
        return dailyLifeQueryRepository.getAllDailyLife(member, lastDailyLifeId, pageSize);
    }

    public Slice<MyDailyLifeSummary> getAllMyDailyLives(final Member member,
                                                        final Long lastDailyLifeId,
                                                        final int pageSize) {
        return dailyLifeQueryRepository.getAllMyDailyLives(member, lastDailyLifeId, pageSize);
    }

    public Slice<DailyLifeSummary> getAllDailyLivesByMember(Member loginMember,
                                                            Long memberId,
                                                            Long lastDailyLifeId,
                                                            int pageSize) {
        return dailyLifeQueryRepository.getAllDailyLivesByMember(loginMember, memberId, lastDailyLifeId, pageSize);
    }

    public long getMyDailyLifeCount(Member member) {
        return dailyLifeQueryRepository.getMyDailyLifeCount(member);
    }
}

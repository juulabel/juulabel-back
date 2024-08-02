package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeCommentSummary;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.DailyLifeCommentJpaRepository;
import com.juu.juulabel.domain.repository.query.DailyLifeCommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class DailyLifeCommentReader {

    private final DailyLifeCommentJpaRepository dailyLifeCommentJpaRepository;
    private final DailyLifeCommentQueryRepository dailyLifeCommentQueryRepository;

    public DailyLifeComment getById(final Long dailyLifeCommentId) {
        return dailyLifeCommentJpaRepository.findByIdAndDeletedAtIsNull(dailyLifeCommentId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_COMMENT));
    }

    public Slice<DailyLifeCommentSummary> getAllByDailyLifeId(
        final Member member,
        final Long dailyLifeId,
        final Long lastCommentId,
        final int pageSize
    ) {
        return dailyLifeCommentQueryRepository.getAllByDailyLifeId(member, dailyLifeId, lastCommentId, pageSize);
    }
}
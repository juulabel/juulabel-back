package com.juu.juulabel.domain.repository.query;


import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.entity.dailylife.QDailyLife;
import com.juu.juulabel.domain.entity.dailylife.QDailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.like.QDailyLifeLike;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailyLifeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDailyLife dailyLife = QDailyLife.dailyLife;
    QDailyLifeComment dailyLifeComment = QDailyLifeComment.dailyLifeComment;
    QDailyLifeLike dailyLifeLike = QDailyLifeLike.dailyLifeLike;

    public DailyLifeDetailInfo getDailyLifeDetailById(Long dailyLifeId) {
        DailyLifeDetailInfo dailyLifeDetailInfo = jpaQueryFactory
            .select(
                Projections.constructor(
                    DailyLifeDetailInfo.class,
                    dailyLife.title,
                    dailyLife.content,
                    dailyLife.id,
                    dailyLife.createdAt,
                    dailyLifeLike.count().as("likeCount"),
                    dailyLifeComment.count().as("commentCount")
                )
            )
            .from(dailyLife)
            .leftJoin(dailyLifeComment).on(dailyLifeComment.dailyLife.eq(dailyLife).and(isNotDeleted(dailyLifeComment)))
            .leftJoin(dailyLifeLike).on(dailyLifeLike.dailyLife.eq(dailyLife))
            .where(
                eqId(dailyLife, dailyLifeId),
                isNotPrivate(dailyLife),
                isNotDeleted(dailyLife)
            )
            .groupBy(dailyLife.id)
            .fetchOne();

        return Optional.ofNullable(dailyLifeDetailInfo)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_DAILY_LIFE));
    }

    private BooleanExpression isNotDeleted(QDailyLife dailyLife) {
        return dailyLife.deletedAt.isNull();
    }

    private BooleanExpression isNotPrivate(QDailyLife dailyLife) {
        return dailyLife.isPrivate.isFalse();
    }

    private BooleanExpression isNotDeleted(QDailyLifeComment dailyLifeComment) {
        return dailyLifeComment.deletedAt.isNull();
    }

    private BooleanExpression eqId(QDailyLife dailyLife, Long dailyLifeId) {
        return dailyLife.id.eq(dailyLifeId);
    }

}

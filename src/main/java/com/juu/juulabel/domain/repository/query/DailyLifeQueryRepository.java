package com.juu.juulabel.domain.repository.query;


import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeSummary;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.entity.dailylife.QDailyLife;
import com.juu.juulabel.domain.entity.dailylife.QDailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.QDailyLifeImage;
import com.juu.juulabel.domain.entity.dailylife.like.QDailyLifeLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailyLifeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDailyLife dailyLife = QDailyLife.dailyLife;
    QDailyLifeComment dailyLifeComment = QDailyLifeComment.dailyLifeComment;
    QDailyLifeLike dailyLifeLike = QDailyLifeLike.dailyLifeLike;
    QDailyLifeImage dailyLifeImage = QDailyLifeImage.dailyLifeImage;

    public DailyLifeDetailInfo getDailyLifeDetailById(Long dailyLifeId, Member member) {
        DailyLifeDetailInfo dailyLifeDetailInfo = jpaQueryFactory
            .select(
                Projections.constructor(
                    DailyLifeDetailInfo.class,
                    dailyLife.title,
                    dailyLife.content,
                    dailyLife.id,
                    Projections.constructor(
                        MemberInfo.class,
                        dailyLife.member.id,
                        dailyLife.member.nickname,
                        dailyLife.member.profileImage
                    ),
                    dailyLife.createdAt,
                    dailyLifeLike.count().as("likeCount"),
                    dailyLifeComment.count().as("commentCount"),
                    isLikedSubQuery(dailyLife, member)
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

    public Slice<DailyLifeSummary> getAllDailyLife(Member member, Long lastDailyLifeId, int pageSize) {
        List<DailyLifeSummary> dailyLifeSummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    DailyLifeSummary.class,
                    dailyLife.title,
                    dailyLife.content,
                    dailyLife.id,
                    Projections.constructor(
                        MemberInfo.class,
                        dailyLife.member.id,
                        dailyLife.member.nickname,
                        dailyLife.member.profileImage
                    ),
                    dailyLifeImage.imagePath.min().as("thumbnailPath"),
                    dailyLifeImage.count().as("imageCount"),
                    dailyLife.createdAt,
                    dailyLifeLike.count().as("likeCount"),
                    dailyLifeComment.count().as("commentCount"),
                    isLikedSubQuery(dailyLife, member)
                )
            )
            .from(dailyLife)
            .leftJoin(dailyLifeComment).on(dailyLifeComment.dailyLife.eq(dailyLife).and(isNotDeleted(dailyLifeComment)))
            .leftJoin(dailyLifeLike).on(dailyLifeLike.dailyLife.eq(dailyLife))
            .leftJoin(dailyLifeImage).on(dailyLifeImage.dailyLife.eq(dailyLife).and(isNotDeleted(dailyLifeImage)))
            .where(
                isNotPrivate(dailyLife),
                isNotDeleted(dailyLife),
                noOffsetByDailyLifeId(dailyLife, lastDailyLifeId)
            )
            .groupBy(dailyLife.id)
            .orderBy(dailyLife.createdAt.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = dailyLifeSummaryList.size() > pageSize;
        if (hasNext) {
            dailyLifeSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(dailyLifeSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    private BooleanExpression noOffsetByDailyLifeId(QDailyLife dailyLife, Long lastDailyLifeId) {
        return Objects.isEmpty(lastDailyLifeId) ? null : dailyLife.id.lt(lastDailyLifeId);
    }

    private BooleanExpression isNotDeleted(QDailyLife dailyLife) {
        return dailyLife.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QDailyLifeImage dailyLifeImage) {
        return dailyLifeImage.deletedAt.isNull();
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

    private BooleanExpression isLikedSubQuery(QDailyLife dailyLife, Member member) {
        return jpaQueryFactory
            .selectFrom(dailyLifeLike)
            .where(
                dailyLifeLike.dailyLife.eq(dailyLife),
                dailyLifeLike.member.eq(member)
            )
            .exists();
    }

}

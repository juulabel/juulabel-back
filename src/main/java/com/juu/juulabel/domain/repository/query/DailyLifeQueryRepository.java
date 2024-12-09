package com.juu.juulabel.domain.repository.query;


import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeSummary;
import com.juu.juulabel.domain.dto.dailylife.MyDailyLifeSummary;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.entity.dailylife.QDailyLife;
import com.juu.juulabel.domain.entity.dailylife.QDailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.QDailyLifeImage;
import com.juu.juulabel.domain.entity.dailylife.like.QDailyLifeLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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
                    dailyLifeLike.countDistinct().as("likeCount"),
                    dailyLifeComment.count().as("commentCount"),
                    isLikedSubQuery(dailyLife, member)
                )
            )
            .from(dailyLife)
            .leftJoin(dailyLifeComment).on(dailyLifeComment.dailyLife.eq(dailyLife).and(isNotDeleted(dailyLifeComment)))
            .leftJoin(dailyLifeLike).on(dailyLifeLike.dailyLife.eq(dailyLife))
            .where(
                eqId(dailyLife, dailyLifeId),
                isNotPrivateOrAuthor(dailyLife, member),
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
                    dailyLifeImage.imagePath.as("thumbnailPath"),
                    getImageCountSubQuery(dailyLife),
                    dailyLife.createdAt,
                    dailyLifeLike.countDistinct().as("likeCount"),
                    dailyLifeComment.count().as("commentCount"),
                    isLikedSubQuery(dailyLife, member)
                )
            )
            .from(dailyLife)
            .leftJoin(dailyLifeComment).on(dailyLifeComment.dailyLife.eq(dailyLife).and(isNotDeleted(dailyLifeComment)))
            .leftJoin(dailyLifeLike).on(dailyLifeLike.dailyLife.eq(dailyLife))
            .leftJoin(dailyLifeImage).on(dailyLifeImage.dailyLife.eq(dailyLife)
                .and(dailyLifeImage.seq.eq(1))
                .and(isNotDeleted(dailyLifeImage)))
            .where(
                isNotPrivateOrAuthor(dailyLife, member),
                isNotDeleted(dailyLife),
                noOffsetByDailyLifeId(dailyLife, lastDailyLifeId)
            )
            .groupBy(dailyLife.id)
            .orderBy(dailyLife.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = dailyLifeSummaryList.size() > pageSize;
        if (hasNext) {
            dailyLifeSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(dailyLifeSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<MyDailyLifeSummary> getAllMyDailyLives(Member member, Long lastDailyLifeId, int pageSize) {
        List<MyDailyLifeSummary> myDailyLifeSummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    MyDailyLifeSummary.class,
                    dailyLife.title,
                    dailyLife.content,
                    dailyLife.id,
                    Projections.constructor(
                        MemberInfo.class,
                        dailyLife.member.id,
                        dailyLife.member.nickname,
                        dailyLife.member.profileImage
                    ),
                    dailyLifeImage.imagePath.as("thumbnailPath"),
                    getImageCountSubQuery(dailyLife),
                    dailyLife.createdAt,
                    dailyLifeLike.countDistinct().as("likeCount"),
                    dailyLifeComment.count().as("commentCount"),
                    dailyLife.isPrivate,
                    isLikedSubQuery(dailyLife, member)
                )
            )
            .from(dailyLife)
            .leftJoin(dailyLifeComment).on(dailyLifeComment.dailyLife.eq(dailyLife).and(isNotDeleted(dailyLifeComment)))
            .leftJoin(dailyLifeLike).on(dailyLifeLike.dailyLife.eq(dailyLife))
            .leftJoin(dailyLifeImage).on(dailyLifeImage.dailyLife.eq(dailyLife)
                .and(dailyLifeImage.seq.eq(1))
                .and(isNotDeleted(dailyLifeImage)))
            .where(
                dailyLife.member.eq(member),
                isNotDeleted(dailyLife),
                noOffsetByDailyLifeId(dailyLife, lastDailyLifeId)
            )
            .groupBy(dailyLife.id)
            .orderBy(dailyLife.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = myDailyLifeSummaryList.size() > pageSize;
        if (hasNext) {
            myDailyLifeSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(myDailyLifeSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<DailyLifeSummary> getAllDailyLivesByMember(Member loginMember, Long memberId, Long lastDailyLifeId, int pageSize) {
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
                    dailyLifeImage.imagePath.as("thumbnailPath"),
                    getImageCountSubQuery(dailyLife),
                    dailyLife.createdAt,
                    dailyLifeLike.countDistinct().as("likeCount"),
                    dailyLifeComment.count().as("commentCount"),
                    isLikedSubQuery(dailyLife, loginMember)
                )
            )
            .from(dailyLife)
            .leftJoin(dailyLifeComment).on(dailyLifeComment.dailyLife.eq(dailyLife).and(isNotDeleted(dailyLifeComment)))
            .leftJoin(dailyLifeLike).on(dailyLifeLike.dailyLife.eq(dailyLife))
            .leftJoin(dailyLifeImage).on(dailyLifeImage.dailyLife.eq(dailyLife)
                .and(dailyLifeImage.seq.eq(1))
                .and(isNotDeleted(dailyLifeImage)))
            .where(
                dailyLife.member.id.eq(memberId),
                isNotPrivateOrAuthor(dailyLife, loginMember),
                isNotDeleted(dailyLife),
                noOffsetByDailyLifeId(dailyLife, lastDailyLifeId)
            )
            .groupBy(dailyLife.id)
            .orderBy(dailyLife.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = dailyLifeSummaryList.size() > pageSize;
        if (hasNext) {
            dailyLifeSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(dailyLifeSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    public long getMyDailyLifeCount(Member member) {
        Long dailyLifeCount = jpaQueryFactory
            .select(dailyLife.count())
            .from(dailyLife)
            .where(
                dailyLife.member.eq(member),
                isNotDeleted(dailyLife)
            )
            .fetchOne();

        return Optional.ofNullable(dailyLifeCount)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_DAILY_LIFE));
    }

    public long getDailyLifeCountByMemberId(Long memberId, Member loginMember) {
        Long dailyLifeCount = jpaQueryFactory
            .select(dailyLife.count())
            .from(dailyLife)
            .where(
                dailyLife.member.id.eq(memberId),
                isNotDeleted(dailyLife),
                isNotPrivateOrAuthor(dailyLife, loginMember)
            )
            .fetchOne();

        return Optional.ofNullable(dailyLifeCount)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_DAILY_LIFE));
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

    private BooleanExpression isNotPrivateOrAuthor(QDailyLife dailyLife, Member member) {
        return dailyLife.isPrivate.isFalse().or(dailyLife.member.eq(member));
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

    private JPQLQuery<Long> getImageCountSubQuery(QDailyLife dailyLife) {
        return JPAExpressions.select(dailyLifeImage.count())
            .from(dailyLifeImage)
            .where(
                dailyLifeImage.dailyLife.eq(dailyLife),
                isNotDeleted(dailyLifeImage)
            );
    }

}

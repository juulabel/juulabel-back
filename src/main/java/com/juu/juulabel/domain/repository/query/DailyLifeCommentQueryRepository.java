package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.comment.CommentSummary;
import com.juu.juulabel.domain.dto.comment.ReplySummary;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.entity.dailylife.QDailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.like.QDailyLifeCommentLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DailyLifeCommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDailyLifeComment dailyLifeComment = QDailyLifeComment.dailyLifeComment;
    QDailyLifeCommentLike dailyLifeCommentLike = QDailyLifeCommentLike.dailyLifeCommentLike;

    public Slice<CommentSummary> getAllByDailyLifeId(Member member, Long dailyLifeId, Long lastCommentId, int pageSize) {
        QDailyLifeComment reply = new QDailyLifeComment("reply");
        List<CommentSummary> commentSummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    CommentSummary.class,
                    dailyLifeComment.content,
                    dailyLifeComment.id,
                    Projections.constructor(
                        MemberInfo.class,
                        dailyLifeComment.member.id,
                        dailyLifeComment.member.nickname,
                        dailyLifeComment.member.profileImage
                    ),
                    dailyLifeComment.createdAt,
                    dailyLifeCommentLike.count().as("likeCount"),
                    JPAExpressions.select(reply.count())
                        .from(reply)
                        .where(
                            reply.parent.id.eq(dailyLifeComment.id),
                            isNotDeleted(reply)
                        ),
                    isLikedSubQuery(dailyLifeComment, member, dailyLifeCommentLike),
                    new CaseBuilder()
                        .when(dailyLifeComment.deletedAt.isNotNull()).then(true)
                        .otherwise(false)
                )
            )
            .from(dailyLifeComment)
            .leftJoin(dailyLifeCommentLike).on(dailyLifeCommentLike.dailyLifeComment.eq(dailyLifeComment))
            .where(
                dailyLifeComment.dailyLife.id.eq(dailyLifeId),
                isNotReply(dailyLifeComment),
                noOffsetByCommentId(dailyLifeComment, lastCommentId)
            )
            .groupBy(dailyLifeComment.id)
            .orderBy(dailyLifeComment.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = commentSummaryList.size() > pageSize;
        if (hasNext) {
            commentSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(commentSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<ReplySummary> getAllRepliesByParentId(Member member, Long dailyLifeId, Long dailyLifeCommentId, Long lastReplyId, int pageSize) {
        List<ReplySummary> replySummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    ReplySummary.class,
                    dailyLifeComment.content,
                    dailyLifeComment.id,
                    Projections.constructor(
                        MemberInfo.class,
                        dailyLifeComment.member.id,
                        dailyLifeComment.member.nickname,
                        dailyLifeComment.member.profileImage
                    ),
                    dailyLifeComment.createdAt,
                    dailyLifeCommentLike.count().as("likeCount"),
                    isLikedSubQuery(dailyLifeComment, member, dailyLifeCommentLike),
                    new CaseBuilder()
                        .when(dailyLifeComment.deletedAt.isNotNull()).then(true)
                        .otherwise(false)
                )
            )
            .from(dailyLifeComment)
            .leftJoin(dailyLifeCommentLike).on(dailyLifeCommentLike.dailyLifeComment.eq(dailyLifeComment))
            .where(
                dailyLifeComment.dailyLife.id.eq(dailyLifeId),
                dailyLifeComment.parent.id.eq(dailyLifeCommentId),
                noOffsetByCommentId(dailyLifeComment, lastReplyId)
            )
            .groupBy(dailyLifeComment.id)
            .orderBy(dailyLifeComment.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = replySummaryList.size() > pageSize;
        if (hasNext) {
            replySummaryList.remove(pageSize);
        }

        return new SliceImpl<>(replySummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    private BooleanExpression isLikedSubQuery(QDailyLifeComment dailyLifeComment, Member member, QDailyLifeCommentLike dailyLifeCommentLike) {
        return jpaQueryFactory
            .selectFrom(dailyLifeComment)
            .where(
                dailyLifeCommentLike.dailyLifeComment.eq(dailyLifeComment),
                dailyLifeCommentLike.member.eq(member)
            )
            .exists();
    }

    private BooleanExpression noOffsetByCommentId(QDailyLifeComment dailyLifeComment, Long lastCommentId) {
        return Objects.isEmpty(lastCommentId) ? null : dailyLifeComment.id.lt(lastCommentId);
    }

    private BooleanExpression isNotReply(QDailyLifeComment dailyLifeComment) {
        return dailyLifeComment.parent.isNull();
    }

    private BooleanExpression isNotDeleted(QDailyLifeComment dailyLifeComment) {
        return dailyLifeComment.deletedAt.isNull();
    }
}

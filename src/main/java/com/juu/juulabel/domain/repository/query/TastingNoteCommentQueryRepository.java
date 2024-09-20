package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.comment.CommentSummary;
import com.juu.juulabel.domain.dto.comment.ReplySummary;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNoteComment;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNoteCommentLike;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
public class TastingNoteCommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QTastingNoteComment tastingNoteComment = QTastingNoteComment.tastingNoteComment;
    QTastingNoteCommentLike tastingNoteCommentLike = QTastingNoteCommentLike.tastingNoteCommentLike;

    public Slice<CommentSummary> getAllByTastingNoteId(Member member, Long tastingNoteId, Long lastCommentId, int pageSize) {
        QTastingNoteComment reply = new QTastingNoteComment("reply");
        List<CommentSummary> commentSummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    CommentSummary.class,
                    tastingNoteComment.content,
                    tastingNoteComment.id,
                    Projections.constructor(
                        MemberInfo.class,
                        tastingNoteComment.member.id,
                        tastingNoteComment.member.nickname,
                        tastingNoteComment.member.profileImage
                    ),
                    tastingNoteComment.createdAt,
                    tastingNoteCommentLike.count().as("likeCount"),
                    JPAExpressions.select(reply.count())
                        .from(reply)
                        .where(
                            reply.parent.id.eq(tastingNoteComment.id),
                            isNotDeleted(reply)
                        ),
                    isLikedSubQuery(tastingNoteComment, member, tastingNoteCommentLike)
                )
            )
            .from(tastingNoteComment)
            .leftJoin(tastingNoteCommentLike).on(tastingNoteCommentLike.tastingNoteComment.eq(tastingNoteComment))
            .where(
                tastingNoteComment.tastingNote.id.eq(tastingNoteId),
                isNotReply(tastingNoteComment),
                isNotDeleted(tastingNoteComment),
                noOffsetByCommentId(tastingNoteComment, lastCommentId)
            )
            .groupBy(tastingNoteComment.id)
            .orderBy(tastingNoteComment.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = commentSummaryList.size() > pageSize;
        if (hasNext) {
            commentSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(commentSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<ReplySummary> getAllRepliesByParentId(Member member, Long tastingNoteId, Long tastingNoteCommentId, Long lastReplyId, int pageSize) {
        List<ReplySummary> replySummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    ReplySummary.class,
                    tastingNoteComment.content,
                    tastingNoteComment.id,
                    Projections.constructor(
                        MemberInfo.class,
                        tastingNoteComment.member.id,
                        tastingNoteComment.member.nickname,
                        tastingNoteComment.member.profileImage
                    ),
                    tastingNoteComment.createdAt,
                    tastingNoteCommentLike.count().as("likeCount"),
                    isLikedSubQuery(tastingNoteComment, member, tastingNoteCommentLike)
                )
            )
            .from(tastingNoteComment)
            .leftJoin(tastingNoteCommentLike).on(tastingNoteCommentLike.tastingNoteComment.eq(tastingNoteComment))
            .where(
                tastingNoteComment.tastingNote.id.eq(tastingNoteId),
                tastingNoteComment.parent.id.eq(tastingNoteCommentId),
                isNotDeleted(tastingNoteComment),
                noOffsetByCommentId(tastingNoteComment, lastReplyId)
            )
            .groupBy(tastingNoteComment.id)
            .orderBy(tastingNoteComment.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = replySummaryList.size() > pageSize;
        if (hasNext) {
            replySummaryList.remove(pageSize);
        }

        return new SliceImpl<>(replySummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    private BooleanExpression isNotDeleted(QTastingNoteComment tastingNoteComment) {
        return tastingNoteComment.deletedAt.isNull();
    }

    private BooleanExpression isLikedSubQuery(QTastingNoteComment tastingNoteComment, Member member, QTastingNoteCommentLike tastingNoteCommentLike) {
        return jpaQueryFactory
            .selectFrom(tastingNoteComment)
            .where(
                tastingNoteCommentLike.tastingNoteComment.eq(tastingNoteComment),
                tastingNoteCommentLike.member.eq(member)
            )
            .exists();
    }

    private BooleanExpression isNotReply(QTastingNoteComment tastingNoteComment) {
        return tastingNoteComment.parent.isNull();
    }

    private BooleanExpression noOffsetByCommentId(QTastingNoteComment tastingNoteComment, Long lastCommentId) {
        return Objects.isEmpty(lastCommentId) ? null : tastingNoteComment.id.lt(lastCommentId);
    }
}

package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.notification.NotificationSummary;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.notification.QNotification;
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

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QNotification notification = QNotification.notification;

    public Slice<NotificationSummary> getAllByMemberId(Long memberId, Long lastNotificationId, int pageSize) {
        List<NotificationSummary> notificationSummaryList = jpaQueryFactory
            .selectDistinct(
                Projections.constructor(
                    NotificationSummary.class,
                    notification.id,
                    notification.relatedUrl,
                    notification.content,
                    notification.notificationType,
                    notification.isRead,
                    notification.createdAt
                )
            )
            .from(notification)
            .where(
                notification.receiver.id.eq(memberId),
                noOffsetByNotificationId(notification, lastNotificationId)
            )
            .orderBy(notification.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = notificationSummaryList.size() > pageSize;
        if (hasNext) {
            notificationSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(notificationSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    public void setNotificationsAsRead(Member member, Long notificationId) {
        long updatedCount = jpaQueryFactory
            .update(notification)
            .set(notification.isRead, true)
            .where(
                notification.id.eq(notificationId),
                notification.receiver.id.eq(member.getId())
            )
            .execute();

        if (updatedCount == 0) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_NOTIFICATION);
        }
    }

    public void setAllNotificationsAsRead(Member member) {
        long updatedCount = jpaQueryFactory
            .update(notification)
            .set(notification.isRead, true)
            .where(
                notification.receiver.id.eq(member.getId())
            )
            .execute();

        if (updatedCount == 0) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_NOTIFICATION);
        }
    }

    public void deleteNotification(Member member, Long notificationId) {
        long deleteCount = jpaQueryFactory
            .delete(notification)
            .where(
                notification.receiver.id.eq(member.getId()),
                notification.id.eq(notificationId)
            )
            .execute();

        if (deleteCount == 0) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_NOTIFICATION);
        }
    }

    private BooleanExpression noOffsetByNotificationId(QNotification notification, Long lastNotificationId) {
        return Objects.isEmpty(lastNotificationId) ? null : notification.id.lt(lastNotificationId);
    }

    public void deleteAllByMember(Member member) {
        long deleteCount = jpaQueryFactory
            .delete(notification)
            .where(
                notification.receiver.id.eq(member.getId())
            )
            .execute();

        if (deleteCount == 0) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_NOTIFICATION);
        }
    }
}

package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.notification.NotificationSummary;
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

    private BooleanExpression noOffsetByNotificationId(QNotification notification, Long lastNotificationId) {
        return Objects.isEmpty(lastNotificationId) ? null : notification.id.lt(lastNotificationId);
    }
}

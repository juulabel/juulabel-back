package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.UsedAlcoholTypeInfo;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlcoholTypeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholType alcoholType = QAlcoholType.alcoholType;

    public List<AlcoholType> getAllByIsUsed() {
        return jpaQueryFactory
            .selectFrom(alcoholType)
            .where(
                isUsed(alcoholType),
                isNotDeleted(alcoholType)
            )
            .fetch();
    }

    public List<UsedAlcoholTypeInfo> getAllUsedAlcoholType() {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    UsedAlcoholTypeInfo.class,
                    alcoholType.id,
                    alcoholType.name,
                    alcoholType.image
                ))
            .from(alcoholType)
            .where(
                isUsed(alcoholType),
                isNotDeleted(alcoholType)
            )
            .fetch();
    }

    private BooleanExpression isUsed(QAlcoholType alcoholType) {
        return alcoholType.isUsed.isTrue();
    }

    private BooleanExpression isNotDeleted(QAlcoholType alcoholType) {
        return alcoholType.deletedAt.isNull();
    }

}
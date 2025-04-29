package com.juu.juulabel.alcohol.repository.query;

import com.juu.juulabel.alcohol.domain.AlcoholType;
import com.juu.juulabel.alcohol.domain.QAlcoholType;
import com.juu.juulabel.alcohol.response.UsedAlcoholTypeInfo;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlcoholTypeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholType alcoholType = QAlcoholType.alcoholType;

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
                isNotDeleted(alcoholType)
            )
            .fetch();
    }

    public AlcoholType getById(Long alcoholTypeId) {
        AlcoholType activeAlcoholType = jpaQueryFactory
            .selectFrom(alcoholType)
            .where(
                eqId(alcoholType, alcoholTypeId),
                isNotDeleted(alcoholType)
            )
            .fetchOne();

        return Optional.ofNullable(activeAlcoholType)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.ALCOHOL_TYPE_NOT_FOUND));
    }

    private BooleanExpression isNotDeleted(QAlcoholType alcoholType) {
        return alcoholType.deletedAt.isNull();
    }

    private BooleanExpression eqId(QAlcoholType alcoholType, Long alcoholTypeId) {
        return alcoholType.id.eq(alcoholTypeId);
    }

}
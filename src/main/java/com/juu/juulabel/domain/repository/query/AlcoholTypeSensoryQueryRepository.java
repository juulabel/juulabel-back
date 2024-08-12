package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholTypeSensory;
import com.juu.juulabel.domain.enums.alcohol.sensory.SensoryType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlcoholTypeSensoryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholTypeSensory alcoholTypeSensory = QAlcoholTypeSensory.alcoholTypeSensory;

    public List<SensoryType> findAllSensoryTypesByAlcoholTypeId(Long alcoholTypeId) {
        return jpaQueryFactory
                .select(
                        alcoholTypeSensory.sensoryType
                )
                .from(alcoholTypeSensory)
                .where(
                        eqAlcoholTypeId(alcoholTypeSensory.alcoholType, alcoholTypeId),
                        isUsed(alcoholTypeSensory)
                )
                .orderBy(
                        alcoholTypeSensory.id.asc()
                )
                .fetch();
    }

    private BooleanExpression eqAlcoholTypeId(QAlcoholType alcoholType, Long alcoholTypeId) {
        return alcoholType.id.eq(alcoholTypeId);
    }

    private BooleanExpression isUsed(QAlcoholTypeSensory alcoholTypeSensory) {
        return alcoholTypeSensory.isUsed.isTrue();
    }

}

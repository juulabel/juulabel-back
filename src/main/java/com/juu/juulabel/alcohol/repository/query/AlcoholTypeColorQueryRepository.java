package com.juu.juulabel.alcohol.repository.query;

import com.juu.juulabel.alcohol.domain.QAlcoholType;
import com.juu.juulabel.alcohol.domain.QAlcoholTypeColor;
import com.juu.juulabel.alcohol.domain.QColor;
import com.juu.juulabel.alcohol.request.ColorInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlcoholTypeColorQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholTypeColor alcoholTypeColor = QAlcoholTypeColor.alcoholTypeColor;
    QColor color = QColor.color;

    public List<ColorInfo> findAllByAlcoholTypeId(Long alcoholTypeId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                ColorInfo.class,
                                color.id,
                                color.name,
                                color.rgb
                        )
                )
                .from(alcoholTypeColor)
                .innerJoin(color).on(alcoholTypeColor.color.eq(color))
                .where(
                        eqAlcoholTypeId(alcoholTypeColor.alcoholType, alcoholTypeId),
                        isUsed(alcoholTypeColor)
                )
                .orderBy(
                        color.id.asc()
                )
                .fetch();
    }

    private BooleanExpression eqAlcoholTypeId(QAlcoholType alcoholType, Long alcoholTypeId) {
        return alcoholType.id.eq(alcoholTypeId);
    }

    private BooleanExpression isUsed(QAlcoholTypeColor qAlcoholTypeColor) {
        return qAlcoholTypeColor.isUsed.isTrue();
    }

}

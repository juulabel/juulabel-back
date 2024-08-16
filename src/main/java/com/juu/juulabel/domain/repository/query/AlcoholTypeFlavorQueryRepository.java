package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.*;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholTypeFlavor;
import com.juu.juulabel.domain.entity.alcohol.QFlavor;
import com.juu.juulabel.domain.entity.alcohol.QFlavorLevel;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AlcoholTypeFlavorQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholTypeFlavor alcoholTypeFlavor = QAlcoholTypeFlavor.alcoholTypeFlavor;
    QFlavor flavor = QFlavor.flavor;
    QFlavorLevel flavorLevel = QFlavorLevel.flavorLevel;

    public List<FlavorLevelInfo> findAllInfoByAlcoholTypeId(Long alcoholTypeId) {
        /*
        key : flavorId
        value : Object (FlavorLevelInfo)
         */
        Map<Long, FlavorLevelInfo> flavorMap = new HashMap<>();

        List<Tuple> flavorTuples = jpaQueryFactory
                .select(
                        flavor.id,
                        flavor.name,
                        flavorLevel.id,
                        flavorLevel.score,
                        flavorLevel.description
                )
                .from(alcoholTypeFlavor)
                .innerJoin(flavor).on(alcoholTypeFlavor.flavor.eq(flavor))
                .innerJoin(flavorLevel).on(flavor.eq(flavorLevel.flavor))
                .where(
                        eqAlcoholTypeId(alcoholTypeFlavor.alcoholType, alcoholTypeId),
                        isUsed(alcoholTypeFlavor)
                )
                .orderBy(
                        flavor.id.asc(),
                        flavorLevel.score.asc()
                )
                .fetch();

        flavorTuples.forEach(f -> {
            Long flavorId = f.get(this.flavor.id);

            FlavorLevelInfo flavorLevelInfo = flavorMap.computeIfAbsent(flavorId,
                    id -> new FlavorLevelInfo(
                            new FlavorInfo(
                                    flavorId,
                                    f.get(this.flavor.name)
                            ),
                            new ArrayList<>()
                    ));

            Level level = new Level(
                    f.get(this.flavorLevel.id),
                    f.get(this.flavorLevel.score),
                    f.get(this.flavorLevel.description)
            );

            flavorLevelInfo.levels().add(level);
        });

        return new ArrayList<>(flavorMap.values());
    }

    private BooleanExpression eqAlcoholTypeId(QAlcoholType alcoholType, Long alcoholTypeId) {
        return alcoholType.id.eq(alcoholTypeId);
    }

    private BooleanExpression isUsed(QAlcoholTypeFlavor alcoholTypeFlavor) {
        return alcoholTypeFlavor.isUsed.isTrue();
    }

}

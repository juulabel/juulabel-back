package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.Level;
import com.juu.juulabel.domain.dto.alcohol.SensoryInfo;
import com.juu.juulabel.domain.dto.alcohol.SensoryLevelInfo;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholTypeSensory;
import com.juu.juulabel.domain.entity.alcohol.QSensory;
import com.juu.juulabel.domain.entity.alcohol.QSensoryLevel;
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
public class AlcoholTypeSensoryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholTypeSensory alcoholTypeSensory = QAlcoholTypeSensory.alcoholTypeSensory;
    QSensory sensory = QSensory.sensory;
    QSensoryLevel sensoryLevel = QSensoryLevel.sensoryLevel;

    public List<SensoryLevelInfo> findAllInfoByAlcoholTypeId(Long alcoholTypeId) {
        /*
        key : sensoryId
        value : Object (SensoryLevelInfo)
         */
        Map<Long, SensoryLevelInfo> sensoryMap = new HashMap<>();

        List<Tuple> sensoryTuples = jpaQueryFactory
                .select(
                        sensory.id,
                        sensory.name,
                        sensoryLevel.id,
                        sensoryLevel.score,
                        sensoryLevel.description
                )
                .from(alcoholTypeSensory)
                .innerJoin(sensory).on(alcoholTypeSensory.sensory.eq(sensory))
                .innerJoin(sensoryLevel).on(sensory.eq(sensoryLevel.sensory))
                .where(
                        eqAlcoholTypeId(alcoholTypeSensory.alcoholType, alcoholTypeId),
                        isUsed(alcoholTypeSensory)
                )
                .orderBy(
                        sensory.id.asc(),
                        sensoryLevel.score.asc()
                )
                .fetch();

        sensoryTuples.forEach(s -> {
            Long sensoryId = s.get(this.sensory.id);

            SensoryLevelInfo sensoryLevelInfo = sensoryMap.computeIfAbsent(sensoryId,
                    id -> new SensoryLevelInfo(
                            new SensoryInfo(
                                    sensoryId,
                                    s.get(this.sensory.name)
                            ),
                            new ArrayList<>()
                    ));

            Level level = new Level(
                    s.get(this.sensoryLevel.id),
                    s.get(this.sensoryLevel.score),
                    s.get(this.sensoryLevel.description)
            );

            sensoryLevelInfo.levels().add(level);
        });

        return new ArrayList<>(sensoryMap.values());
    }

    private BooleanExpression eqAlcoholTypeId(QAlcoholType alcoholType, Long alcoholTypeId) {
        return alcoholType.id.eq(alcoholTypeId);
    }

    private BooleanExpression isUsed(QAlcoholTypeSensory alcoholTypeSensory) {
        return alcoholTypeSensory.isUsed.isTrue();
    }

}

package com.juu.juulabel.alcohol.repository.query;

import com.juu.juulabel.alcohol.domain.QSensory;
import com.juu.juulabel.alcohol.domain.QSensoryLevel;
import com.juu.juulabel.alcohol.response.UsedSensoryInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SensoryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QSensory sensory = QSensory.sensory;
    QSensoryLevel sensoryLevel = QSensoryLevel.sensoryLevel;


    public List<UsedSensoryInfo> getAllUsedSensory() {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    UsedSensoryInfo.class,
                    sensory.id,
                    sensory.name,
                    sensoryLevel.id,
                    sensoryLevel.description,
                    sensoryLevel.score
                )
            )
            .from(sensory)
            .leftJoin(sensoryLevel).on(sensoryLevel.sensory.eq(sensory))
            .fetch();
    }
}

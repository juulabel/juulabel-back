package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.UsedSensoryInfo;
import com.juu.juulabel.domain.entity.alcohol.QSensory;
import com.juu.juulabel.domain.entity.alcohol.QSensoryLevel;
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

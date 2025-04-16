package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.UsedFlavorInfo;
import com.juu.juulabel.domain.entity.alcohol.QFlavor;
import com.juu.juulabel.domain.entity.alcohol.QFlavorLevel;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FlavorQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QFlavor flavor = QFlavor.flavor;
    QFlavorLevel flavorLevel = QFlavorLevel.flavorLevel;

    public List<UsedFlavorInfo> getAllUsedFlavor() {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    UsedFlavorInfo.class,
                    flavor.id,
                    flavor.name,
                    flavorLevel.id,
                    flavorLevel.description,
                    flavorLevel.score
                )
            )
            .from(flavor)
            .leftJoin(flavorLevel).on(flavorLevel.flavor.eq(flavor))
            .fetch();
    }

}

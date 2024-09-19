package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicBrewerySummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QBrewery;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BreweryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QBrewery brewery = QBrewery.brewery;
    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;

    public List<AlcoholicBrewerySummary> getBreweryDetailById(Long breweryId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                AlcoholicBrewerySummary.class,
                                alcoholicDrinks.id,
                                alcoholicDrinks.name,
                                alcoholicDrinks.alcoholType.name,
                                alcoholicDrinks.image
                                )
                )
                .from(brewery)
                .leftJoin(alcoholicDrinks).on(alcoholicDrinks.brewery.id.eq(brewery.id))
                .where(eqBreweryId(breweryId), isNotDeleted(brewery))
                .fetch();
    }

    public BrewerySummary getBreweryById(Long breweryId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                BrewerySummary.class,
                                brewery.id,
                                brewery.name,
                                brewery.region
                        )
                )
                .from(brewery)
                .where(eqBreweryId(breweryId), isNotDeleted(brewery))
                .fetchOne();
    }

    private BooleanExpression eqBreweryId(Long breweryId) {
        return brewery.id.eq(breweryId);
    }

    private BooleanExpression isNotDeleted(QBrewery brewery) {
        return brewery.deletedAt.isNull();
    }
}

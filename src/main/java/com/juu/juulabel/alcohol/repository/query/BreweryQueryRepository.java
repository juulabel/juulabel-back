package com.juu.juulabel.alcohol.repository.query;

import com.juu.juulabel.alcohol.domain.QAlcoholicDrinks;
import com.juu.juulabel.alcohol.domain.QBrewery;
import com.juu.juulabel.alcohol.response.AlcoholicBrewerySummary;
import com.juu.juulabel.alcohol.response.BrewerySummary;
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
                                brewery.region,
                                brewery.message
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

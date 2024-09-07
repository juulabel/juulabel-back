package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.entity.member.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.juu.juulabel.domain.dto.alcohol.AlcoholTypeSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QBrewery;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlcoholDrinksTypeQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(AlcoholDrinksTypeQueryRepository.class);
    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QBrewery brewery = QBrewery.brewery;

    public Slice<AlcoholSearchSummary> findByAlcoholType(Member member, Long alcoholTypeId, int pageSize) {
        List<AlcoholSearchSummary> alcoholicDrinksList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                AlcoholSearchSummary.class,
                                alcoholicDrinks.id,
                                alcoholicDrinks.name,
                                alcoholicDrinks.image,
                                brewery.name
//                                Projections.constructor(
//                                        BrewerySummary.class,
//                                        brewery.id,
//                                        brewery.name,
//                                        brewery.region
//                                )
                                )
                )
                .from(alcoholicDrinks)
                .innerJoin(alcoholType).on(alcoholicDrinks.alcoholType.eq(alcoholType))
                .where(eqAlcoholTypeId(alcoholType,alcoholTypeId),
                        isNotDeleted(alcoholicDrinks)
                )
                .orderBy(alcoholicDrinksNameAse(alcoholicDrinks))
                .limit(pageSize+ 1L )
                .fetch();

        log.debug("Fetched data size: {}", alcoholicDrinksList.size());
        log.debug("Fetched data: {}", alcoholicDrinksList);


        boolean hasNext = alcoholicDrinksList.size() > pageSize;
        if (hasNext) {
            alcoholicDrinksList.remove(pageSize);
        }
        return new SliceImpl<>(alcoholicDrinksList, PageRequest.ofSize(pageSize), hasNext);
    }

    public long countByAlcoholType(Long alcoholTypeId) {
        return jpaQueryFactory
                .select(alcoholicDrinks.count())
                .from(alcoholicDrinks)
                .innerJoin(alcoholType).on(alcoholicDrinks.alcoholType.eq(alcoholType))
                .where(eqAlcoholTypeId(alcoholType,alcoholTypeId), isNotDeleted(alcoholicDrinks))
                .fetchOne();
    }

    private OrderSpecifier<String> alcoholicDrinksNameAse(QAlcoholicDrinks alcoholicDrinks){
        return alcoholicDrinks.name.asc();
    }

    private BooleanExpression eqAlcoholTypeId(QAlcoholType AlcoholType, Long alcoholTypeId) {
        return alcoholType.id.eq(alcoholTypeId);
    }

    private BooleanExpression isNotDeleted(QAlcoholicDrinks alcoholicDrinks) {
        return alcoholicDrinks.deletedAt.isNull();
    }
}

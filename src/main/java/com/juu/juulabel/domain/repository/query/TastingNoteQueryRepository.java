package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.AlcoholTypeSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
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
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class TastingNoteQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QBrewery brewery = QBrewery.brewery;

    public Slice<AlcoholicDrinksSummary> findAllAlcoholicDrinks(String search, String lastAlcoholicDrinksName, int pageSize) {
        List<AlcoholicDrinksSummary> alcoholicDrinksList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                AlcoholicDrinksSummary.class,
                                alcoholicDrinks.id,
                                alcoholicDrinks.name,
                                alcoholicDrinks.image,
                                Projections.constructor(
                                        AlcoholTypeSummary.class,
                                        alcoholType.id,
                                        alcoholType.name
                                ),
                                Projections.constructor(
                                        BrewerySummary.class,
                                        brewery.id,
                                        brewery.name
                                )
                        )
                )
                .from(alcoholicDrinks)
                .innerJoin(alcoholType).on(alcoholicDrinks.alcoholType.eq(alcoholType))
                .innerJoin(brewery).on(alcoholicDrinks.brewery.eq(brewery))
                .where(
                        containSearch(alcoholicDrinks, search),
                        noOffsetAlcoholicDrinksName(alcoholicDrinks, lastAlcoholicDrinksName)
                )
                .orderBy(alcoholicDrinksNameAsc(alcoholicDrinks))
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = alcoholicDrinksList.size() > pageSize;
        if (hasNext) {
            alcoholicDrinksList.remove(pageSize);
        }

        return new SliceImpl<>(alcoholicDrinksList, PageRequest.ofSize(pageSize), hasNext);
    }

    private OrderSpecifier<String> alcoholicDrinksNameAsc(QAlcoholicDrinks alcoholicDrinks) {
        return alcoholicDrinks.name.asc();
    }

    private BooleanExpression containSearch(QAlcoholicDrinks alcoholicDrinks, String search) {
        if (Objects.isNull(search) || search.isEmpty()) {
            return null;
        }

        return alcoholicDrinks.name.containsIgnoreCase(search)
                .or(brewery.name.containsIgnoreCase(search));
    }

    private BooleanExpression noOffsetAlcoholicDrinksName(QAlcoholicDrinks alcoholicDrinks, String lastAlcoholicDrinksName) {
        if (Objects.isNull(lastAlcoholicDrinksName)) {
            return null;
        }

        return alcoholicDrinks.name.gt(lastAlcoholicDrinksName);
    }


}

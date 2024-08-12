package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlcoholicDrinksQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;

    public AlcoholicDrinks findById(Long id) {
        return jpaQueryFactory
                .selectFrom(alcoholicDrinks)
                .where(
                        eqAlcoholicDrinksId(alcoholicDrinks, id),
                        isNotDeleted(alcoholicDrinks)
                )
                .fetchOne();
    }

    private BooleanExpression eqAlcoholicDrinksId(QAlcoholicDrinks alcoholicDrinks, Long alcoholicDrinksId) {
        return alcoholicDrinks.id.eq(alcoholicDrinksId);
    }

    private BooleanExpression isNotDeleted(QAlcoholicDrinks alcoholicDrinks) {
        return alcoholicDrinks.deletedAt.isNull();
    }
}

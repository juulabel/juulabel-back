package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.*;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QBrewery;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNote;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class AlcoholDrinksDetailQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QBrewery brewery = QBrewery.brewery;
    QTastingNote tastingNote = QTastingNote.tastingNote;

    public AlcoholicDrinksDetailInfo findAlcoholDrinksDetailById(Long alcoholDrinksId){
        AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo = jpaQueryFactory
                .select(
                        Projections.constructor(
                                AlcoholicDrinksDetailInfo.class,
                                alcoholicDrinks.id,
                                alcoholicDrinks.name,
                                alcoholicDrinks.image,
                                alcoholicDrinks.alcoholContent,
                                alcoholicDrinks.volume,
                                alcoholicDrinks.price,
                                alcoholicDrinks.ingredients,
                                Projections.constructor(
                                        AlcoholTypeSummary.class,
                                        alcoholType.id,
                                        alcoholType.name
                                ),
                                Projections.constructor(
                                        BrewerySummary.class,
                                        brewery.id,
                                        brewery.name,
                                        brewery.region
                                )
//                                Projections.constructor(
//                                        TastingNoteSummary.class,
//                                        tastingNote.color,
//                                        tastingNote.turbidity,
//                                        tastingNote.carbonation,
//                                        tastingNote.viscosity,
//                                        tastingNote.scent
//                                )
                        )
                )
                .from(alcoholicDrinks)
                .leftJoin(alcoholicDrinks.alcoholType, alcoholType)
                .leftJoin(alcoholicDrinks.brewery, brewery)
                .where(eqAlcoholDrinkId(alcoholDrinksId),
                        isNotDeleted(alcoholicDrinks))
                .fetchOne();

        return Optional.ofNullable(alcoholicDrinksDetailInfo).orElseThrow(()-> new InvalidParamException(ErrorCode.NOT_FOUND_ALCOHOLIC_DRINKS_TYPE)
        );
    }

    private BooleanExpression eqAlcoholDrinkId(Long alcoholDrinksId){
        return alcoholicDrinks.id.eq(alcoholDrinksId);
    }

    private BooleanExpression isNotDeleted(QAlcoholicDrinks alcoholicDrinks){
        return alcoholicDrinks.deletedAt.isNull();
    }

}

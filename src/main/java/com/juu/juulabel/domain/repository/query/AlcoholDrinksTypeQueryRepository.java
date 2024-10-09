package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNote;
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

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QBrewery brewery = QBrewery.brewery;
    QTastingNote tastingNote = QTastingNote.tastingNote;

    public Slice<AlcoholSearchSummary> findByAlcoholType(Long alcoholTypeId, int pageSize, String arrayType) {
        List<AlcoholSearchSummary> alcoholicDrinksList = jpaQueryFactory
                .select(Projections.constructor(
                        AlcoholSearchSummary.class,
                        alcoholicDrinks.id,
                        alcoholicDrinks.name,
                        alcoholicDrinks.image,
                        brewery.name,
                        tastingNote.rating.avg().as("averageRating")
                ))
                .from(alcoholicDrinks)
                .leftJoin(tastingNote).on(tastingNote.alcoholicDrinks.eq(alcoholicDrinks))
                .innerJoin(alcoholType).on(alcoholicDrinks.alcoholType.eq(alcoholType))
                .where(
                        eqAlcoholTypeId(alcoholType, alcoholTypeId),
                        isNotDeleted(alcoholicDrinks)
                )
                // 동적 정렬 적용
                .orderBy(getOrderSpecifier(arrayType, tastingNote))
                .groupBy(alcoholicDrinks.id)
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = alcoholicDrinksList.size() > pageSize;
        if (hasNext) {
            alcoholicDrinksList.remove(pageSize);
        }

        return new SliceImpl<>(alcoholicDrinksList, PageRequest.ofSize(pageSize), hasNext);
    }

    // 동적 정렬 로직
    private OrderSpecifier<?> getOrderSpecifier(String arrayType, QTastingNote tastingNote) {
        if ("priceLow".equals(arrayType)) {
            return alcoholicDrinks.price.asc();  // 가격 낮은 순
        } else if ("priceHigh".equals(arrayType)) {
            return alcoholicDrinks.price.desc();  // 가격 높은 순
        }
        else if ("ratingHigh".equals(arrayType)) {// 평균 평점 높은 순
            return tastingNote.rating.avg().desc();
        }
        else if ("tastingNoteCount".equals(arrayType)) { // 시음노트 갯수 많은 순
            return tastingNote.id.count().desc();
        }
        else {
            return alcoholicDrinks.name.asc();  // 기본 정렬: 이름 순
        }
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

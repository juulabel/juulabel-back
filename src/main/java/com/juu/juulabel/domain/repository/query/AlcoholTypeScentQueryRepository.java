package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.CategoryWithScentSummary;
import com.juu.juulabel.domain.dto.alcohol.ScentSummary;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholTypeScent;
import com.juu.juulabel.domain.entity.alcohol.QScent;
import com.juu.juulabel.domain.entity.category.QCategory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AlcoholTypeScentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholTypeScent alcoholTypeScent = QAlcoholTypeScent.alcoholTypeScent;
    QCategory category = QCategory.category;
    QScent scent = QScent.scent;

    public List<CategoryWithScentSummary> findAllCategoryWithScentByAlcoholTypeId(Long alcoholTypeId) {
        // TODO scent -> List 변경 필요

        Map<Long, CategoryWithScentSummary> categoryMap = new HashMap<>();

        List<Tuple> fetch = jpaQueryFactory
                .select(
                        category.id,
                        category.code,
                        category.name,
                        scent.id,
                        scent.name
                )
                .from(alcoholTypeScent)
                .innerJoin(category).on(alcoholTypeScent.category.eq(category))
                .innerJoin(scent).on(alcoholTypeScent.scent.eq(scent))
                .where(
                        eqAlcoholTypeId(alcoholTypeScent.alcoholType, alcoholTypeId),
                        isUsed(alcoholTypeScent),
                        isUsed(category)
                )
                .orderBy(
                        category.id.asc(),
                        scent.id.asc()
                )
                .fetch();

        fetch.forEach(tuple -> {
            Long categoryId = tuple.get(category.id);
            CategoryWithScentSummary categorySummary = categoryMap.get(categoryId);

            if (categorySummary == null) {
                categorySummary = new CategoryWithScentSummary(
                        categoryId,
                        tuple.get(category.code),
                        tuple.get(category.name),
                        new ArrayList<>()
                );
                categoryMap.put(categoryId, categorySummary);
            }

            ScentSummary scentSummary = new ScentSummary(
                    tuple.get(scent.id),
                    tuple.get(scent.name)
            );

            categorySummary.scents().add(scentSummary);
        });

        return new ArrayList<>(categoryMap.values());
//        return jpaQueryFactory
//                .select(
//                        Projections.constructor(
//                                CategoryWithScentSummary.class,
//                                category.id,
//                                category.code,
//                                category.name,
//                                Projections.constructor(
//                                        ScentSummary.class,
//                                        scent.id,
//                                        scent.name
//                                )
//                        )
//                )
//                .from(alcoholTypeScent)
//                .innerJoin(category).on(alcoholTypeScent.category.eq(category))
//                .innerJoin(scent).on(alcoholTypeScent.scent.eq(scent))
//                .where(
//                        eqAlcoholTypeId(alcoholTypeScent.alcoholType, alcoholTypeId),
//                        isUsed(alcoholTypeScent),
//                        isUsed(category)
//                )
//                .orderBy(
//                        category.id.asc(),
//                        scent.id.asc()
//                )
//                .fetch();
    }

    private BooleanExpression eqAlcoholTypeId(QAlcoholType alcoholType, Long alcoholTypeId) {
        return alcoholType.id.eq(alcoholTypeId);
    }

    private BooleanExpression isUsed(QAlcoholTypeScent alcoholTypeScent) {
        return alcoholTypeScent.isUsed.isTrue();
    }

    private BooleanExpression isUsed(QCategory category) {
        return category.isUsed.isTrue();
    }

}

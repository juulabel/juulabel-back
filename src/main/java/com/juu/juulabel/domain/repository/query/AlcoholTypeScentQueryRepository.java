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

import java.util.*;

@Repository
@RequiredArgsConstructor
public class AlcoholTypeScentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholTypeScent alcoholTypeScent = QAlcoholTypeScent.alcoholTypeScent;
    QCategory category = QCategory.category;
    QScent scent = QScent.scent;

    public List<CategoryWithScentSummary> findAllCategoryWithScentByAlcoholTypeId(Long alcoholTypeId) {
        /*
        key : categoryId
        value : Object (CategoryWithScentSummary)
         */
        Map<Long, CategoryWithScentSummary> categoryMap = new HashMap<>();

        List<Tuple> categoryTuples = jpaQueryFactory
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

        categoryTuples.forEach(c -> {
            Long categoryId = c.get(this.category.id);

            CategoryWithScentSummary categories = categoryMap.computeIfAbsent(categoryId,
                    id -> new CategoryWithScentSummary(
                            id,
                            c.get(this.category.code),
                            c.get(this.category.name),
                            new ArrayList<>()
                    ));

            ScentSummary scentSummary = new ScentSummary(
                    c.get(this.scent.id),
                    c.get(this.scent.name)
            );

            categories.scents().add(scentSummary);
        });

        return new ArrayList<>(categoryMap.values());
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

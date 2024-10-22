package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.AlcoholTypeSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QBrewery;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.QMemberAlcoholicDrinks;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlcoholicDrinksQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QBrewery brewery = QBrewery.brewery;
    QMemberAlcoholicDrinks memberAlcoholicDrinks = QMemberAlcoholicDrinks.memberAlcoholicDrinks;

    public AlcoholicDrinks findById(Long id) {
        return jpaQueryFactory
                .selectFrom(alcoholicDrinks)
                .where(
                        eqAlcoholicDrinksId(alcoholicDrinks, id),
                        isNotDeleted(alcoholicDrinks)
                )
                .fetchOne();
    }

    public Slice<AlcoholicDrinksSummary> getAllMyAlcoholicDrinks(Member member, Long lastAlcoholicDrinksId, int pageSize) {
        List<AlcoholicDrinksSummary> myAlcoholicDrinksList = jpaQueryFactory
            .select(
                Projections.constructor(
                    AlcoholicDrinksSummary.class,
                    alcoholicDrinks.id,
                    alcoholicDrinks.name,
                    alcoholicDrinks.image,
                    alcoholicDrinks.alcoholContent,
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
                )
            )
            .from(alcoholicDrinks)
            .innerJoin(alcoholType).on(alcoholicDrinks.alcoholType.eq(alcoholType))
            .innerJoin(brewery).on(alcoholicDrinks.brewery.eq(brewery))
            .innerJoin(memberAlcoholicDrinks).on(memberAlcoholicDrinks.alcoholicDrinks.eq(alcoholicDrinks))
            .where(
                memberAlcoholicDrinks.member.eq(member),
                isNotDeleted(alcoholicDrinks),
                noOffsetByAlcoholicDrinksId(alcoholicDrinks, lastAlcoholicDrinksId)
            )
            .orderBy(alcoholicDrinks.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = myAlcoholicDrinksList.size() > pageSize;
        if (hasNext) {
            myAlcoholicDrinksList.remove(pageSize);
        }

        return new SliceImpl<>(myAlcoholicDrinksList, PageRequest.ofSize(pageSize), hasNext);
    }

    public List<String> getRelatedSearchByKeyword(String keyword) {
        return jpaQueryFactory
                .select(alcoholicDrinks.name.concat(" ")
                        .concat(alcoholicDrinks.volume.stringValue()))
                .from(alcoholicDrinks)
                .where(alcoholicDrinks.name.contains(keyword), isNotDeleted(alcoholicDrinks))
                .fetch();
    }

    private BooleanExpression noOffsetByAlcoholicDrinksId(QAlcoholicDrinks alcoholicDrinks, Long lastAlcoholicDrinksId) {
        return Objects.isEmpty(lastAlcoholicDrinksId) ? null : alcoholicDrinks.id.lt(lastAlcoholicDrinksId);
    }

    private BooleanExpression eqAlcoholicDrinksId(QAlcoholicDrinks alcoholicDrinks, Long alcoholicDrinksId) {
        return alcoholicDrinks.id.eq(alcoholicDrinksId);
    }

    private BooleanExpression isNotDeleted(QAlcoholicDrinks alcoholicDrinks) {
        return alcoholicDrinks.deletedAt.isNull();
    }
}

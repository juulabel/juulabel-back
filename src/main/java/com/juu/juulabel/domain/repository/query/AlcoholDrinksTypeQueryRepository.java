package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.QBrewery;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNote;
import com.juu.juulabel.domain.enums.sort.SortType;
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
public class AlcoholDrinksTypeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QBrewery brewery = QBrewery.brewery;
    QTastingNote tastingNote = QTastingNote.tastingNote;

    public Slice<AlcoholSearchSummary> findByAlcoholType(Long alcoholTypeId, String lastAlcoholicDrinksName,int pageSize, SortType sortType) {
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
                        isNotDeleted(alcoholicDrinks),
                        noOffsetAlcoholicDrinksName(alcoholicDrinks, lastAlcoholicDrinksName)
                )
                // 동적 정렬 적용
                .orderBy(getOrderSpecifier(sortType))
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
    private OrderSpecifier<?> getOrderSpecifier(SortType sortType) {
        switch (sortType) {
            case PRICE_LOW:
                return alcoholicDrinks.discountPrice.asc();  // 가격 낮은 순
            case PRICE_HIGH:
                return alcoholicDrinks.discountPrice.desc();  // 가격 높은 순
            case RATING_HIGH:// 평균 평점 높은 순
                return alcoholicDrinks.rating.desc();
            case TASTING_NOTE_COUNT_HIGH: // 시음노트 갯수 많은 순
                return alcoholicDrinks.tastingNoteCount.desc();
            default:
                return alcoholicDrinks.name.asc();  // 기본 정렬: 이름 순
        }
    }


        public long countByAlcoholType (Long alcoholTypeId){
            return jpaQueryFactory
                    .select(alcoholicDrinks.count())
                    .from(alcoholicDrinks)
                    .innerJoin(alcoholType).on(alcoholicDrinks.alcoholType.eq(alcoholType))
                    .where(eqAlcoholTypeId(alcoholType, alcoholTypeId), isNotDeleted(alcoholicDrinks))
                    .fetchOne();
        }

        private OrderSpecifier<String> alcoholicDrinksNameAse (QAlcoholicDrinks alcoholicDrinks){
            return alcoholicDrinks.name.asc();
        }

        private BooleanExpression eqAlcoholTypeId (QAlcoholType AlcoholType, Long alcoholTypeId){
            return alcoholType.id.eq(alcoholTypeId);
        }

        private BooleanExpression isNotDeleted (QAlcoholicDrinks alcoholicDrinks){
            return alcoholicDrinks.deletedAt.isNull();
        }

        private BooleanExpression noOffsetAlcoholicDrinksName(QAlcoholicDrinks alcoholicDrinks, String lastAlcoholicDrinksName) {
            if (Objects.isNull(lastAlcoholicDrinksName)) {
                return null;
            }

            return alcoholicDrinks.name.gt(lastAlcoholicDrinksName);
        }
    }

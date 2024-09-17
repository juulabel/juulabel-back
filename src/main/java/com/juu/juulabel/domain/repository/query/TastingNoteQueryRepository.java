package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.alcohol.AlcoholTypeSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSummary;
import com.juu.juulabel.domain.entity.alcohol.*;
import com.juu.juulabel.domain.entity.member.Member;
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
    QTastingNote tastingNote = QTastingNote.tastingNote;
    QTastingNoteImage tastingNoteImage = QTastingNoteImage.tastingNoteImage;

    public Slice<AlcoholicDrinksSummary> findAllAlcoholicDrinks(String search, String lastAlcoholicDrinksName, int pageSize) {
        List<AlcoholicDrinksSummary> alcoholicDrinksList = jpaQueryFactory
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

    public Slice<TastingNoteSummary> getAllTastingNotes(Member member, Long lastTastingNoteId, int pageSize) {
        List<TastingNoteSummary> tastingNoteSummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    TastingNoteSummary.class,
                    tastingNote.id,
                    tastingNote.alcoholDrinksInfo.alcoholicDrinksName,
                    Projections.constructor(
                        MemberInfo.class,
                        tastingNote.member.id,
                        tastingNote.member.nickname,
                        tastingNote.member.profileImage
                    ),
                    tastingNoteImage.imagePath.as("thumbnailPath"),
                    tastingNote.alcoholDrinksInfo.alcoholTypeName,
                    tastingNote.createdAt,
                    hasMultipleImagesSubQuery(tastingNote, tastingNoteImage)
                )
            )
            .from(tastingNote)
            .leftJoin(tastingNoteImage).on(tastingNoteImage.tastingNote.eq(tastingNote)
                .and(tastingNoteImage.seq.eq(1))
                .and(isNotDeleted(tastingNoteImage)))
            .where(
                isNotPrivate(tastingNote),
                isNotDeleted(tastingNote),
                noOffsetByTastingNoteId(tastingNote, lastTastingNoteId)
            )
            .groupBy(tastingNote.id)
            .orderBy(tastingNote.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = tastingNoteSummaryList.size() > pageSize;
        if (hasNext) {
            tastingNoteSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(tastingNoteSummaryList, PageRequest.ofSize(pageSize), hasNext);
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

    private BooleanExpression noOffsetByTastingNoteId(QTastingNote tastingNote, Long lastTastingNoteId) {
        return io.jsonwebtoken.lang.Objects.isEmpty(lastTastingNoteId) ? null : tastingNote.id.lt(lastTastingNoteId);
    }

    private BooleanExpression isNotPrivate(QTastingNote tastingNote) {
        return tastingNote.isPrivate.isFalse();
    }

    private BooleanExpression isNotDeleted(QTastingNote tastingNote) {
        return tastingNote.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QTastingNoteImage tastingNoteImage) {
        return tastingNoteImage.deletedAt.isNull();
    }

    private BooleanExpression hasMultipleImagesSubQuery(QTastingNote tastingNote, QTastingNoteImage tastingNoteImage) {
        return jpaQueryFactory
            .selectFrom(tastingNoteImage)
            .where(
                tastingNoteImage.tastingNote.eq(tastingNote),
                isNotDeleted(tastingNoteImage)
            )
            .groupBy(tastingNoteImage.tastingNote)
            .having(tastingNoteImage.count().goe(2))
            .exists();
    }

}

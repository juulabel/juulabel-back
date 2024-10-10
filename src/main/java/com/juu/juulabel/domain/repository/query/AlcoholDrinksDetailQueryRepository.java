package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.api.service.terms.TermsService;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.*;
import com.juu.juulabel.domain.dto.tastingnote.LikeTopTastingNoteSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSensorSummary;
import com.juu.juulabel.domain.entity.alcohol.*;
import com.juu.juulabel.domain.entity.member.QMember;
import com.juu.juulabel.domain.entity.tastingnote.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class AlcoholDrinksDetailQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final TermsService termsService;

    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QBrewery brewery = QBrewery.brewery;
    QTastingNote tastingNote = QTastingNote.tastingNote;
    QTastingNoteLike tastingNoteLike = QTastingNoteLike.tastingNoteLike;
    QTastingNoteFlavorLevel tastingNoteFlavorLevel = QTastingNoteFlavorLevel.tastingNoteFlavorLevel;
    QFlavor flavor = QFlavor.flavor;
    QFlavorLevel flavorLevel = QFlavorLevel.flavorLevel;
    QTastingNoteScent tastingNoteScent = QTastingNoteScent.tastingNoteScent;
    QTastingNoteSensoryLevel tastingNoteSensoryLevel = QTastingNoteSensoryLevel.tastingNoteSensoryLevel;
    QSensoryLevel sensoryLevel = QSensoryLevel.sensoryLevel;
    QSensory sensory = QSensory.sensory;
    QMember member = QMember.member;

    public AlcoholicDrinksDetailInfo findAlcoholDrinksDetailById(Long alcoholDrinksId) {

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
                                alcoholicDrinks.rating,
                                alcoholicDrinks.tastingNoteCount,
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
                .leftJoin(alcoholicDrinks.alcoholType, alcoholType)
                .leftJoin(alcoholicDrinks.brewery, brewery)
                // .leftJoin(alcoholicDrinks.tastingNotes, tastingNote)
                .where(eqAlcoholDrinkId(alcoholDrinksId),
                        isNotDeleted(alcoholicDrinks))
                .fetchOne();

        return Optional.ofNullable(alcoholicDrinksDetailInfo).orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_ALCOHOLIC_DRINKS_TYPE)
        );
    }

    // 전통주 id에 따른 좋아요 제일 많이 받은 시음노트 id 가져오기
    public Long findMostLikedTastingNoteId(Long alcoholDrinksId) {
        return jpaQueryFactory
                .select(tastingNote.id)
                .from(tastingNote)
                .leftJoin(tastingNoteLike)
                .on(tastingNote.id.eq(tastingNoteLike.tastingNote.id))
                .where(tastingNote.alcoholicDrinks.id.eq(alcoholDrinksId))
                .groupBy(tastingNote.id)
                .orderBy(tastingNoteLike.id.count().desc())
                .limit(1)
                .fetchFirst();  // 첫 번째 결과만 가져오기
    }

    public TastingNoteSensorSummary getTastingNoteSensor(Long mostLikedTastingNoteId) {
        String rgb = getColor(mostLikedTastingNoteId);
        List<String> scentList = getScentList(mostLikedTastingNoteId);
        List<FlavorDetail> flavorList = getFlavorList(mostLikedTastingNoteId);
        List<SensoryDetail> senseryList = getSenseryList(mostLikedTastingNoteId);

        return new TastingNoteSensorSummary(mostLikedTastingNoteId, rgb, scentList, flavorList, senseryList);
    }

    public Double getAverageRating(Long alcoholDrinksId) {
        return jpaQueryFactory
                .select(tastingNote.rating.avg())
                .from(tastingNote)
                .where(tastingNote.alcoholicDrinks.id.eq(alcoholDrinksId)
                        .and(tastingNote.deletedAt.isNull()))
                .fetchOne();
    }

    public Long getTastingNoteCount(Long alcoholDrinksId) {
        return jpaQueryFactory
                .select(tastingNote.count())
                .from(tastingNote)
                .where(tastingNote.alcoholicDrinks.id.eq(alcoholDrinksId)
                        .and(tastingNote.deletedAt.isNull()))
                .fetchOne();
    }

    private String getColor(Long mostLikedTastingNoteId) {
        return jpaQueryFactory
                .select(tastingNote.color.rgb)
                .from(tastingNote)
                .where(tastingNote.id.eq(mostLikedTastingNoteId))
                .limit(1)
                .fetchOne();
    }

    private List<String> getScentList(Long mostLikedTastingNoteId) {
        return jpaQueryFactory
                .select(tastingNoteScent.scent.name)
                .from(tastingNoteScent)
                .where(tastingNoteScent.tastingNote.id.eq(mostLikedTastingNoteId))
                .fetch();
    }

    private List<FlavorDetail> getFlavorList(Long mostLikedTastingNoteId) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        FlavorDetail.class,
                        flavor.name,
                        flavorLevel.score
                ))
                .from(tastingNoteFlavorLevel)
                .join(flavorLevel.flavor, flavor)
                .where(tastingNoteFlavorLevel.tastingNote.id.eq(mostLikedTastingNoteId))
                .fetch();
    }

    private List<SensoryDetail> getSenseryList(Long mostLikedTastingNoteId) {
        return jpaQueryFactory
                //.select(tastingNoteSensoryLevel.sensoryLevel.sensory.name)
                .select(Projections.constructor(
                        SensoryDetail.class,
                        sensory.name,
                        sensoryLevel.score
                ))
                .from(tastingNoteSensoryLevel)
                .join(sensoryLevel.sensory, sensory)
                .where(tastingNoteSensoryLevel.tastingNote.id.eq(mostLikedTastingNoteId))
                .fetch();
    }

    public List<LikeTopTastingNoteSummary> getTastingNoteList(Long alcoholDrinksId) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        LikeTopTastingNoteSummary.class,
                        member.profileImage,
                        member.nickname,
                        tastingNoteLike.tastingNote.id.count().as("count"),
                        tastingNote.content
                ))
                .from(tastingNoteLike)
                .leftJoin(tastingNoteLike.tastingNote, tastingNote)
                .leftJoin(tastingNote.member, member)
                .where(tastingNote.alcoholicDrinks.id.eq(alcoholDrinksId))
                .groupBy(tastingNote.id)
                .orderBy(tastingNoteLike.tastingNote.id.count().desc())
                .limit(2)
                .fetch();
    }

    private BooleanExpression eqAlcoholDrinkId(Long alcoholDrinksId) {
        return alcoholicDrinks.id.eq(alcoholDrinksId);
    }

    private BooleanExpression isNotDeleted(QAlcoholicDrinks alcoholicDrinks) {
        return alcoholicDrinks.deletedAt.isNull();
    }


    // 시음노트별로 좋아요 갯수
    // TEST
    public List<Tuple> findTastingNoteLikesCount(Long alcoholDrinksId) {
        return jpaQueryFactory
                .select(tastingNote.id, tastingNoteLike.id.count())
                .from(tastingNote)
                .leftJoin(tastingNoteLike)
                .on(tastingNote.id.eq(tastingNoteLike.tastingNote.id))
                .where(tastingNote.alcoholicDrinks.id.eq(alcoholDrinksId))
                .groupBy(tastingNote.id)
                .fetch();
    }


}

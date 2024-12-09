package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.AlcoholTypeSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.alcohol.BrewerySummary;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.dto.tastingnote.MyTastingNoteSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteDetailInfo;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSummary;
import com.juu.juulabel.domain.entity.alcohol.*;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.*;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TastingNoteQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAlcoholType alcoholType = QAlcoholType.alcoholType;
    QAlcoholicDrinks alcoholicDrinks = QAlcoholicDrinks.alcoholicDrinks;
    QBrewery brewery = QBrewery.brewery;
    QTastingNote tastingNote = QTastingNote.tastingNote;
    QTastingNoteImage tastingNoteImage = QTastingNoteImage.tastingNoteImage;
    QTastingNoteSensoryLevel tastingNoteSensoryLevel = QTastingNoteSensoryLevel.tastingNoteSensoryLevel;
    QSensoryLevel sensoryLevel = QSensoryLevel.sensoryLevel;
    QTastingNoteScent tastingNoteScent = QTastingNoteScent.tastingNoteScent;
    QScent scent = QScent.scent;
    QTastingNoteFlavorLevel tastingNoteFlavorLevel = QTastingNoteFlavorLevel.tastingNoteFlavorLevel;
    QFlavorLevel flavorLevel = QFlavorLevel.flavorLevel;
    QTastingNoteLike tastingNoteLike = QTastingNoteLike.tastingNoteLike;
    QTastingNoteComment tastingNoteComment = QTastingNoteComment.tastingNoteComment;

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
                                        brewery.region,
                                        brewery.message
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
                isNotPrivateOrAuthor(tastingNote, member),
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

    public Slice<TastingNoteSummary> getAllTastingNotesByAlcoholicDrinksId(Member member, Long lastTastingNoteId, int pageSize, Long alcoholicDrinksId) {
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
                isNotPrivateOrAuthor(tastingNote, member),
                isNotDeleted(tastingNote),
                noOffsetByTastingNoteId(tastingNote, lastTastingNoteId),
                tastingNote.alcoholicDrinks.id.eq(alcoholicDrinksId)
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

    public Slice<MyTastingNoteSummary> getAllMyTastingNotes(Member member, Long lastTastingNoteId, int pageSize) {
        List<MyTastingNoteSummary> myTastingNoteSummaryList = jpaQueryFactory
            .select(
                Projections.constructor(
                    MyTastingNoteSummary.class,
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
                    hasMultipleImagesSubQuery(tastingNote, tastingNoteImage),
                    tastingNote.isPrivate
                )
            )
            .from(tastingNote)
            .leftJoin(tastingNoteImage).on(tastingNoteImage.tastingNote.eq(tastingNote)
                .and(tastingNoteImage.seq.eq(1))
                .and(isNotDeleted(tastingNoteImage)))
            .where(
                tastingNote.member.eq(member),
                isNotDeleted(tastingNote),
                noOffsetByTastingNoteId(tastingNote, lastTastingNoteId)
            )
            .groupBy(tastingNote.id)
            .orderBy(tastingNote.id.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = myTastingNoteSummaryList.size() > pageSize;
        if (hasNext) {
            myTastingNoteSummaryList.remove(pageSize);
        }

        return new SliceImpl<>(myTastingNoteSummaryList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<TastingNoteSummary> getAllTastingNotesByMember(Member loginMember, Long memberId, Long lastTastingNoteId, int pageSize) {
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
                tastingNote.member.id.eq(memberId),
                isNotPrivateOrAuthor(tastingNote, loginMember),
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

    public long countBySearch (String search){
        return jpaQueryFactory
                .select(alcoholicDrinks.count())
                .from(alcoholicDrinks)
                .where(
                        alcoholicDrinks.name.contains(search),
                        isNotDeleted(alcoholicDrinks)
                )
                .fetchOne();
    }

    public long getMyTastingNoteCount(Member member) {
        Long tastingNoteCount = jpaQueryFactory
            .select(tastingNote.count())
            .from(tastingNote)
            .where(
                tastingNote.member.eq(member),
                isNotDeleted(tastingNote)
            )
            .fetchOne();

        return Optional.ofNullable(tastingNoteCount)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TASTING_NOTE));
    }

    public long getTastingNoteCountByMemberId(Long memberId, Member loginMember) {
        Long tastingNoteCount = jpaQueryFactory
            .select(tastingNote.count())
            .from(tastingNote)
            .where(
                tastingNote.member.id.eq(memberId),
                isNotPrivateOrAuthor(tastingNote, loginMember),
                isNotDeleted(tastingNote)
            )
            .fetchOne();

        return Optional.ofNullable(tastingNoteCount)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TASTING_NOTE));
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

    private BooleanExpression isNotPrivateOrAuthor(QTastingNote tastingNote, Member member) {
        return tastingNote.isPrivate.isFalse().or(tastingNote.member.eq(member));
    }

    private BooleanExpression isNotDeleted(QTastingNote tastingNote) {
        return tastingNote.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QTastingNoteImage tastingNoteImage) {
        return tastingNoteImage.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted (QAlcoholicDrinks alcoholicDrinks){
        return alcoholicDrinks.deletedAt.isNull();
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

    public TastingNoteDetailInfo getTastingNoteDetailById(Long tastingNoteId, Member member) {
        TastingNoteDetailInfo tastingNoteDetailInfo = jpaQueryFactory
            .select(
                Projections.constructor(
                    TastingNoteDetailInfo.class,
                    tastingNote.id,
                    Projections.constructor(
                        MemberInfo.class,
                        tastingNote.member.id,
                        tastingNote.member.nickname,
                        tastingNote.member.profileImage
                    ),
                    tastingNote.createdAt,
                    tastingNote.alcoholDrinksInfo.alcoholicDrinksName,
                    tastingNote.alcoholDrinksInfo.alcoholTypeName,
                    tastingNote.alcoholDrinksInfo.alcoholContent,
                    tastingNote.alcoholDrinksInfo.breweryName,
                    tastingNote.color.rgb,
//                    getSensoryLevelIds(tastingNote, tastingNoteSensoryLevel, sensoryLevel),
//                    getScentIds(tastingNote, tastingNoteScent, scent),
//                    getFlavorLevelIds(tastingNote, tastingNoteFlavorLevel, flavorLevel),
                    tastingNote.content,
                    tastingNote.rating,
                    tastingNoteLike.countDistinct().as("likeCount"),
                    tastingNoteComment.count().as("commentCount"),
                    isLikedSubQuery(tastingNote, member)
                )
            )
            .from(tastingNote)
            .leftJoin(tastingNoteLike).on(tastingNoteLike.tastingNote.eq(tastingNote))
            .leftJoin(tastingNoteComment).on(tastingNoteComment.tastingNote.eq(tastingNote))
            .where(
                eqId(tastingNote, tastingNoteId),
                isNotPrivateOrAuthor(tastingNote, member),
                isNotDeleted(tastingNote)
            )
            .groupBy(tastingNote.id)
            .fetchOne();

        return Optional.ofNullable(tastingNoteDetailInfo)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TASTING_NOTE));
    }

    public List<Long> getSensoryLevelIds(Long tastingNoteId, Member member) {
        return jpaQueryFactory
            .select(sensoryLevel.id)
            .from(tastingNoteSensoryLevel)
            .join(tastingNoteSensoryLevel.sensoryLevel, sensoryLevel)
            .where(
                tastingNoteSensoryLevel.tastingNote.eq(tastingNote),
                eqId(tastingNote, tastingNoteId),
                isNotPrivateOrAuthor(tastingNote, member),
                isNotDeleted(tastingNote)
            )
            .fetch();
    }

    public List<Long> getScentIds(Long tastingNoteId, Member member) {
        return jpaQueryFactory
            .select(scent.id)
            .from(tastingNoteScent)
            .join(tastingNoteScent.scent, scent)
            .where(
                tastingNoteScent.tastingNote.eq(tastingNote),
                eqId(tastingNote, tastingNoteId),
                isNotPrivateOrAuthor(tastingNote, member),
                isNotDeleted(tastingNote)
            )
            .fetch();
    }

    public List<Long> getFlavorLevelIds(Long tastingNoteId, Member member) {
        return jpaQueryFactory
            .select(flavorLevel.id)
            .from(tastingNoteFlavorLevel)
            .join(tastingNoteFlavorLevel.flavorLevel, flavorLevel)
            .where(
                tastingNoteFlavorLevel.tastingNote.eq(tastingNote),
                eqId(tastingNote, tastingNoteId),
                isNotPrivateOrAuthor(tastingNote, member),
                isNotDeleted(tastingNote)
            )
            .fetch();
    }

    private BooleanExpression isLikedSubQuery(QTastingNote tastingNote, Member member) {
        return jpaQueryFactory
            .selectFrom(tastingNoteLike)
            .where(
                tastingNoteLike.tastingNote.eq(tastingNote),
                tastingNoteLike.member.eq(member)
            )
            .exists();
    }

    private BooleanExpression eqId(QTastingNote tastingNote, Long tastingNoteId) {
        return tastingNote.id.eq(tastingNoteId);
    }

    public Long getAlcoholicDrinksByTastingNoteId(Long tastingNoteId) {
        return jpaQueryFactory
            .select(alcoholicDrinks.id)
            .from(tastingNote)
            .where(
                eqId(tastingNote, tastingNoteId)
            )
            .fetchOne();
    }
}

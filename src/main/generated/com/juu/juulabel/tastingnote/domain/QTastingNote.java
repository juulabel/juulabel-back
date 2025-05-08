package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNote is a Querydsl query type for TastingNote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNote extends EntityPathBase<TastingNote> {

    private static final long serialVersionUID = 544443447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNote tastingNote = new QTastingNote("tastingNote");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final com.juu.juulabel.tastingnote.domain.embedded.QAlcoholicDrinksSnapshot alcoholDrinksInfo;

    public final com.juu.juulabel.alcohol.domain.QAlcoholicDrinks alcoholicDrinks;

    public final com.juu.juulabel.alcohol.domain.QAlcoholType alcoholType;

    public final com.juu.juulabel.alcohol.domain.QColor color;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPrivate = createBoolean("isPrivate");

    public final com.juu.juulabel.member.domain.QMember member;

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final ListPath<TastingNoteScent, QTastingNoteScent> tastingNoteScents = this.<TastingNoteScent, QTastingNoteScent>createList("tastingNoteScents", TastingNoteScent.class, QTastingNoteScent.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTastingNote(String variable) {
        this(TastingNote.class, forVariable(variable), INITS);
    }

    public QTastingNote(Path<? extends TastingNote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNote(PathMetadata metadata, PathInits inits) {
        this(TastingNote.class, metadata, inits);
    }

    public QTastingNote(Class<? extends TastingNote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholDrinksInfo = inits.isInitialized("alcoholDrinksInfo") ? new com.juu.juulabel.tastingnote.domain.embedded.QAlcoholicDrinksSnapshot(forProperty("alcoholDrinksInfo")) : null;
        this.alcoholicDrinks = inits.isInitialized("alcoholicDrinks") ? new com.juu.juulabel.alcohol.domain.QAlcoholicDrinks(forProperty("alcoholicDrinks"), inits.get("alcoholicDrinks")) : null;
        this.alcoholType = inits.isInitialized("alcoholType") ? new com.juu.juulabel.alcohol.domain.QAlcoholType(forProperty("alcoholType")) : null;
        this.color = inits.isInitialized("color") ? new com.juu.juulabel.alcohol.domain.QColor(forProperty("color")) : null;
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
    }

}


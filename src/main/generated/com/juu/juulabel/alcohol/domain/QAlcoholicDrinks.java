package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholicDrinks is a Querydsl query type for AlcoholicDrinks
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholicDrinks extends EntityPathBase<AlcoholicDrinks> {

    private static final long serialVersionUID = -695932244L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlcoholicDrinks alcoholicDrinks = new QAlcoholicDrinks("alcoholicDrinks");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final NumberPath<Double> alcoholContent = createNumber("alcoholContent", Double.class);

    public final QAlcoholType alcoholType;

    public final QBrewery brewery;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> discountPrice = createNumber("discountPrice", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final NumberPath<Integer> regularPrice = createNumber("regularPrice", Integer.class);

    public final NumberPath<Integer> tastingNoteCount = createNumber("tastingNoteCount", Integer.class);

    public final ListPath<com.juu.juulabel.tastingnote.domain.TastingNote, com.juu.juulabel.tastingnote.domain.QTastingNote> tastingNotes = this.<com.juu.juulabel.tastingnote.domain.TastingNote, com.juu.juulabel.tastingnote.domain.QTastingNote>createList("tastingNotes", com.juu.juulabel.tastingnote.domain.TastingNote.class, com.juu.juulabel.tastingnote.domain.QTastingNote.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> volume = createNumber("volume", Integer.class);

    public QAlcoholicDrinks(String variable) {
        this(AlcoholicDrinks.class, forVariable(variable), INITS);
    }

    public QAlcoholicDrinks(Path<? extends AlcoholicDrinks> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlcoholicDrinks(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlcoholicDrinks(PathMetadata metadata, PathInits inits) {
        this(AlcoholicDrinks.class, metadata, inits);
    }

    public QAlcoholicDrinks(Class<? extends AlcoholicDrinks> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholType = inits.isInitialized("alcoholType") ? new QAlcoholType(forProperty("alcoholType")) : null;
        this.brewery = inits.isInitialized("brewery") ? new QBrewery(forProperty("brewery")) : null;
    }

}


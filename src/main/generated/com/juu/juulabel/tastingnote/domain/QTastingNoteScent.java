package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNoteScent is a Querydsl query type for TastingNoteScent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNoteScent extends EntityPathBase<TastingNoteScent> {

    private static final long serialVersionUID = 2021566116L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNoteScent tastingNoteScent = new QTastingNoteScent("tastingNoteScent");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.alcohol.domain.QScent scent;

    public final QTastingNote tastingNote;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTastingNoteScent(String variable) {
        this(TastingNoteScent.class, forVariable(variable), INITS);
    }

    public QTastingNoteScent(Path<? extends TastingNoteScent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNoteScent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNoteScent(PathMetadata metadata, PathInits inits) {
        this(TastingNoteScent.class, metadata, inits);
    }

    public QTastingNoteScent(Class<? extends TastingNoteScent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.scent = inits.isInitialized("scent") ? new com.juu.juulabel.alcohol.domain.QScent(forProperty("scent")) : null;
        this.tastingNote = inits.isInitialized("tastingNote") ? new QTastingNote(forProperty("tastingNote"), inits.get("tastingNote")) : null;
    }

}


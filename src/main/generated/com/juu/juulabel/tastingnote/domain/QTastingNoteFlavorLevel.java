package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNoteFlavorLevel is a Querydsl query type for TastingNoteFlavorLevel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNoteFlavorLevel extends EntityPathBase<TastingNoteFlavorLevel> {

    private static final long serialVersionUID = -2092980529L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNoteFlavorLevel tastingNoteFlavorLevel = new QTastingNoteFlavorLevel("tastingNoteFlavorLevel");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.juu.juulabel.alcohol.domain.QFlavorLevel flavorLevel;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTastingNote tastingNote;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTastingNoteFlavorLevel(String variable) {
        this(TastingNoteFlavorLevel.class, forVariable(variable), INITS);
    }

    public QTastingNoteFlavorLevel(Path<? extends TastingNoteFlavorLevel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNoteFlavorLevel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNoteFlavorLevel(PathMetadata metadata, PathInits inits) {
        this(TastingNoteFlavorLevel.class, metadata, inits);
    }

    public QTastingNoteFlavorLevel(Class<? extends TastingNoteFlavorLevel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flavorLevel = inits.isInitialized("flavorLevel") ? new com.juu.juulabel.alcohol.domain.QFlavorLevel(forProperty("flavorLevel"), inits.get("flavorLevel")) : null;
        this.tastingNote = inits.isInitialized("tastingNote") ? new QTastingNote(forProperty("tastingNote"), inits.get("tastingNote")) : null;
    }

}


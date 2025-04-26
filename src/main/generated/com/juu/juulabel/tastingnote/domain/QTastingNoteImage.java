package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNoteImage is a Querydsl query type for TastingNoteImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNoteImage extends EntityPathBase<TastingNoteImage> {

    private static final long serialVersionUID = 2012624740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNoteImage tastingNoteImage = new QTastingNoteImage("tastingNoteImage");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QTastingNote tastingNote;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTastingNoteImage(String variable) {
        this(TastingNoteImage.class, forVariable(variable), INITS);
    }

    public QTastingNoteImage(Path<? extends TastingNoteImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNoteImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNoteImage(PathMetadata metadata, PathInits inits) {
        this(TastingNoteImage.class, metadata, inits);
    }

    public QTastingNoteImage(Class<? extends TastingNoteImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tastingNote = inits.isInitialized("tastingNote") ? new QTastingNote(forProperty("tastingNote"), inits.get("tastingNote")) : null;
    }

}


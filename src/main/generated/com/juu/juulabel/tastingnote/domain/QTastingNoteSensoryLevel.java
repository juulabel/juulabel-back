package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNoteSensoryLevel is a Querydsl query type for TastingNoteSensoryLevel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNoteSensoryLevel extends EntityPathBase<TastingNoteSensoryLevel> {

    private static final long serialVersionUID = 751400092L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNoteSensoryLevel tastingNoteSensoryLevel = new QTastingNoteSensoryLevel("tastingNoteSensoryLevel");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.alcohol.domain.QSensoryLevel sensoryLevel;

    public final QTastingNote tastingNote;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTastingNoteSensoryLevel(String variable) {
        this(TastingNoteSensoryLevel.class, forVariable(variable), INITS);
    }

    public QTastingNoteSensoryLevel(Path<? extends TastingNoteSensoryLevel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNoteSensoryLevel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNoteSensoryLevel(PathMetadata metadata, PathInits inits) {
        this(TastingNoteSensoryLevel.class, metadata, inits);
    }

    public QTastingNoteSensoryLevel(Class<? extends TastingNoteSensoryLevel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sensoryLevel = inits.isInitialized("sensoryLevel") ? new com.juu.juulabel.alcohol.domain.QSensoryLevel(forProperty("sensoryLevel"), inits.get("sensoryLevel")) : null;
        this.tastingNote = inits.isInitialized("tastingNote") ? new QTastingNote(forProperty("tastingNote"), inits.get("tastingNote")) : null;
    }

}


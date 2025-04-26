package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSensoryLevel is a Querydsl query type for SensoryLevel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSensoryLevel extends EntityPathBase<SensoryLevel> {

    private static final long serialVersionUID = -1898460580L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSensoryLevel sensoryLevel = new QSensoryLevel("sensoryLevel");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final QSensory sensory;

    public QSensoryLevel(String variable) {
        this(SensoryLevel.class, forVariable(variable), INITS);
    }

    public QSensoryLevel(Path<? extends SensoryLevel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSensoryLevel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSensoryLevel(PathMetadata metadata, PathInits inits) {
        this(SensoryLevel.class, metadata, inits);
    }

    public QSensoryLevel(Class<? extends SensoryLevel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sensory = inits.isInitialized("sensory") ? new QSensory(forProperty("sensory")) : null;
    }

}


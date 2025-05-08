package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholTypeSensory is a Querydsl query type for AlcoholTypeSensory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholTypeSensory extends EntityPathBase<AlcoholTypeSensory> {

    private static final long serialVersionUID = 932258254L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlcoholTypeSensory alcoholTypeSensory = new QAlcoholTypeSensory("alcoholTypeSensory");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final QAlcoholType alcoholType;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    public final QSensory sensory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAlcoholTypeSensory(String variable) {
        this(AlcoholTypeSensory.class, forVariable(variable), INITS);
    }

    public QAlcoholTypeSensory(Path<? extends AlcoholTypeSensory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlcoholTypeSensory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlcoholTypeSensory(PathMetadata metadata, PathInits inits) {
        this(AlcoholTypeSensory.class, metadata, inits);
    }

    public QAlcoholTypeSensory(Class<? extends AlcoholTypeSensory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholType = inits.isInitialized("alcoholType") ? new QAlcoholType(forProperty("alcoholType")) : null;
        this.sensory = inits.isInitialized("sensory") ? new QSensory(forProperty("sensory")) : null;
    }

}


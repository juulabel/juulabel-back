package com.juu.juulabel.dailylife.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyLifeImage is a Querydsl query type for DailyLifeImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyLifeImage extends EntityPathBase<DailyLifeImage> {

    private static final long serialVersionUID = -1561443708L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyLifeImage dailyLifeImage = new QDailyLifeImage("dailyLifeImage");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDailyLife dailyLife;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDailyLifeImage(String variable) {
        this(DailyLifeImage.class, forVariable(variable), INITS);
    }

    public QDailyLifeImage(Path<? extends DailyLifeImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyLifeImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyLifeImage(PathMetadata metadata, PathInits inits) {
        this(DailyLifeImage.class, metadata, inits);
    }

    public QDailyLifeImage(Class<? extends DailyLifeImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dailyLife = inits.isInitialized("dailyLife") ? new QDailyLife(forProperty("dailyLife"), inits.get("dailyLife")) : null;
    }

}


package com.juu.juulabel.dailylife.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyLife is a Querydsl query type for DailyLife
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyLife extends EntityPathBase<DailyLife> {

    private static final long serialVersionUID = 153685271L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyLife dailyLife = new QDailyLife("dailyLife");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPrivate = createBoolean("isPrivate");

    public final com.juu.juulabel.member.domain.QMember member;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDailyLife(String variable) {
        this(DailyLife.class, forVariable(variable), INITS);
    }

    public QDailyLife(Path<? extends DailyLife> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyLife(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyLife(PathMetadata metadata, PathInits inits) {
        this(DailyLife.class, metadata, inits);
    }

    public QDailyLife(Class<? extends DailyLife> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
    }

}


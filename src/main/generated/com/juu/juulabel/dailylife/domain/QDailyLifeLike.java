package com.juu.juulabel.dailylife.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyLifeLike is a Querydsl query type for DailyLifeLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyLifeLike extends EntityPathBase<DailyLifeLike> {

    private static final long serialVersionUID = 88264014L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyLifeLike dailyLifeLike = new QDailyLifeLike("dailyLifeLike");

    public final com.juu.juulabel.common.base.QBaseCreatedTimeEntity _super = new com.juu.juulabel.common.base.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDailyLife dailyLife;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.member.domain.QMember member;

    public QDailyLifeLike(String variable) {
        this(DailyLifeLike.class, forVariable(variable), INITS);
    }

    public QDailyLifeLike(Path<? extends DailyLifeLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyLifeLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyLifeLike(PathMetadata metadata, PathInits inits) {
        this(DailyLifeLike.class, metadata, inits);
    }

    public QDailyLifeLike(Class<? extends DailyLifeLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dailyLife = inits.isInitialized("dailyLife") ? new QDailyLife(forProperty("dailyLife"), inits.get("dailyLife")) : null;
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
    }

}


package com.juu.juulabel.dailylife.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyLifeComment is a Querydsl query type for DailyLifeComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyLifeComment extends EntityPathBase<DailyLifeComment> {

    private static final long serialVersionUID = 1729618248L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyLifeComment dailyLifeComment = new QDailyLifeComment("dailyLifeComment");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDailyLife dailyLife;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.member.domain.QMember member;

    public final QDailyLifeComment parent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDailyLifeComment(String variable) {
        this(DailyLifeComment.class, forVariable(variable), INITS);
    }

    public QDailyLifeComment(Path<? extends DailyLifeComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyLifeComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyLifeComment(PathMetadata metadata, PathInits inits) {
        this(DailyLifeComment.class, metadata, inits);
    }

    public QDailyLifeComment(Class<? extends DailyLifeComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dailyLife = inits.isInitialized("dailyLife") ? new QDailyLife(forProperty("dailyLife"), inits.get("dailyLife")) : null;
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
        this.parent = inits.isInitialized("parent") ? new QDailyLifeComment(forProperty("parent"), inits.get("parent")) : null;
    }

}


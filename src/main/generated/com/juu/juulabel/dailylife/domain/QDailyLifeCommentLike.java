package com.juu.juulabel.dailylife.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyLifeCommentLike is a Querydsl query type for DailyLifeCommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyLifeCommentLike extends EntityPathBase<DailyLifeCommentLike> {

    private static final long serialVersionUID = 1784291583L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyLifeCommentLike dailyLifeCommentLike = new QDailyLifeCommentLike("dailyLifeCommentLike");

    public final com.juu.juulabel.common.base.QBaseCreatedTimeEntity _super = new com.juu.juulabel.common.base.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDailyLifeComment dailyLifeComment;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.member.domain.QMember member;

    public QDailyLifeCommentLike(String variable) {
        this(DailyLifeCommentLike.class, forVariable(variable), INITS);
    }

    public QDailyLifeCommentLike(Path<? extends DailyLifeCommentLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyLifeCommentLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyLifeCommentLike(PathMetadata metadata, PathInits inits) {
        this(DailyLifeCommentLike.class, metadata, inits);
    }

    public QDailyLifeCommentLike(Class<? extends DailyLifeCommentLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dailyLifeComment = inits.isInitialized("dailyLifeComment") ? new QDailyLifeComment(forProperty("dailyLifeComment"), inits.get("dailyLifeComment")) : null;
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
    }

}


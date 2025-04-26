package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNoteLike is a Querydsl query type for TastingNoteLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNoteLike extends EntityPathBase<TastingNoteLike> {

    private static final long serialVersionUID = 1727577198L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNoteLike tastingNoteLike = new QTastingNoteLike("tastingNoteLike");

    public final com.juu.juulabel.common.base.QBaseCreatedTimeEntity _super = new com.juu.juulabel.common.base.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.member.domain.QMember member;

    public final QTastingNote tastingNote;

    public QTastingNoteLike(String variable) {
        this(TastingNoteLike.class, forVariable(variable), INITS);
    }

    public QTastingNoteLike(Path<? extends TastingNoteLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNoteLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNoteLike(PathMetadata metadata, PathInits inits) {
        this(TastingNoteLike.class, metadata, inits);
    }

    public QTastingNoteLike(Class<? extends TastingNoteLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
        this.tastingNote = inits.isInitialized("tastingNote") ? new QTastingNote(forProperty("tastingNote"), inits.get("tastingNote")) : null;
    }

}


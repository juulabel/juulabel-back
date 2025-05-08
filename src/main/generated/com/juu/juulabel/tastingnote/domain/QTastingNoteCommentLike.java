package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNoteCommentLike is a Querydsl query type for TastingNoteCommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNoteCommentLike extends EntityPathBase<TastingNoteCommentLike> {

    private static final long serialVersionUID = -670110241L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNoteCommentLike tastingNoteCommentLike = new QTastingNoteCommentLike("tastingNoteCommentLike");

    public final com.juu.juulabel.common.base.QBaseCreatedTimeEntity _super = new com.juu.juulabel.common.base.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.member.domain.QMember member;

    public final QTastingNoteComment tastingNoteComment;

    public QTastingNoteCommentLike(String variable) {
        this(TastingNoteCommentLike.class, forVariable(variable), INITS);
    }

    public QTastingNoteCommentLike(Path<? extends TastingNoteCommentLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNoteCommentLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNoteCommentLike(PathMetadata metadata, PathInits inits) {
        this(TastingNoteCommentLike.class, metadata, inits);
    }

    public QTastingNoteCommentLike(Class<? extends TastingNoteCommentLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
        this.tastingNoteComment = inits.isInitialized("tastingNoteComment") ? new QTastingNoteComment(forProperty("tastingNoteComment"), inits.get("tastingNoteComment")) : null;
    }

}


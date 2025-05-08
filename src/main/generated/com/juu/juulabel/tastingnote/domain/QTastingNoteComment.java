package com.juu.juulabel.tastingnote.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTastingNoteComment is a Querydsl query type for TastingNoteComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTastingNoteComment extends EntityPathBase<TastingNoteComment> {

    private static final long serialVersionUID = 435559976L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTastingNoteComment tastingNoteComment = new QTastingNoteComment("tastingNoteComment");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.juu.juulabel.member.domain.QMember member;

    public final QTastingNoteComment parent;

    public final QTastingNote tastingNote;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTastingNoteComment(String variable) {
        this(TastingNoteComment.class, forVariable(variable), INITS);
    }

    public QTastingNoteComment(Path<? extends TastingNoteComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTastingNoteComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTastingNoteComment(PathMetadata metadata, PathInits inits) {
        this(TastingNoteComment.class, metadata, inits);
    }

    public QTastingNoteComment(Class<? extends TastingNoteComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.juu.juulabel.member.domain.QMember(forProperty("member")) : null;
        this.parent = inits.isInitialized("parent") ? new QTastingNoteComment(forProperty("parent"), inits.get("parent")) : null;
        this.tastingNote = inits.isInitialized("tastingNote") ? new QTastingNote(forProperty("tastingNote"), inits.get("tastingNote")) : null;
    }

}


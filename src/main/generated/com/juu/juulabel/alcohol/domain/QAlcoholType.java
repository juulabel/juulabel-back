package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholType is a Querydsl query type for AlcoholType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholType extends EntityPathBase<AlcoholType> {

    private static final long serialVersionUID = -1838556943L;

    public static final QAlcoholType alcoholType = new QAlcoholType("alcoholType");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final ListPath<com.juu.juulabel.tastingnote.domain.TastingNote, com.juu.juulabel.tastingnote.domain.QTastingNote> tastingNotes = this.<com.juu.juulabel.tastingnote.domain.TastingNote, com.juu.juulabel.tastingnote.domain.QTastingNote>createList("tastingNotes", com.juu.juulabel.tastingnote.domain.TastingNote.class, com.juu.juulabel.tastingnote.domain.QTastingNote.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAlcoholType(String variable) {
        super(AlcoholType.class, forVariable(variable));
    }

    public QAlcoholType(Path<? extends AlcoholType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlcoholType(PathMetadata metadata) {
        super(AlcoholType.class, metadata);
    }

}


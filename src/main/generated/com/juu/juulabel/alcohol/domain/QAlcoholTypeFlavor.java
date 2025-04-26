package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholTypeFlavor is a Querydsl query type for AlcoholTypeFlavor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholTypeFlavor extends EntityPathBase<AlcoholTypeFlavor> {

    private static final long serialVersionUID = -336025873L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlcoholTypeFlavor alcoholTypeFlavor = new QAlcoholTypeFlavor("alcoholTypeFlavor");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final QAlcoholType alcoholType;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QFlavor flavor;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAlcoholTypeFlavor(String variable) {
        this(AlcoholTypeFlavor.class, forVariable(variable), INITS);
    }

    public QAlcoholTypeFlavor(Path<? extends AlcoholTypeFlavor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlcoholTypeFlavor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlcoholTypeFlavor(PathMetadata metadata, PathInits inits) {
        this(AlcoholTypeFlavor.class, metadata, inits);
    }

    public QAlcoholTypeFlavor(Class<? extends AlcoholTypeFlavor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholType = inits.isInitialized("alcoholType") ? new QAlcoholType(forProperty("alcoholType")) : null;
        this.flavor = inits.isInitialized("flavor") ? new QFlavor(forProperty("flavor")) : null;
    }

}


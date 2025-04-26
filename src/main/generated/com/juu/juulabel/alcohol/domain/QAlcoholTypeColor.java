package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholTypeColor is a Querydsl query type for AlcoholTypeColor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholTypeColor extends EntityPathBase<AlcoholTypeColor> {

    private static final long serialVersionUID = -1121889038L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlcoholTypeColor alcoholTypeColor = new QAlcoholTypeColor("alcoholTypeColor");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final QAlcoholType alcoholType;

    public final QColor color;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAlcoholTypeColor(String variable) {
        this(AlcoholTypeColor.class, forVariable(variable), INITS);
    }

    public QAlcoholTypeColor(Path<? extends AlcoholTypeColor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlcoholTypeColor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlcoholTypeColor(PathMetadata metadata, PathInits inits) {
        this(AlcoholTypeColor.class, metadata, inits);
    }

    public QAlcoholTypeColor(Class<? extends AlcoholTypeColor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholType = inits.isInitialized("alcoholType") ? new QAlcoholType(forProperty("alcoholType")) : null;
        this.color = inits.isInitialized("color") ? new QColor(forProperty("color")) : null;
    }

}


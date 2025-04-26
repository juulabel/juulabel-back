package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholTypeScent is a Querydsl query type for AlcoholTypeScent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholTypeScent extends EntityPathBase<AlcoholTypeScent> {

    private static final long serialVersionUID = -1107476950L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlcoholTypeScent alcoholTypeScent = new QAlcoholTypeScent("alcoholTypeScent");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final QAlcoholType alcoholType;

    public final com.juu.juulabel.category.domain.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    public final QScent scent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAlcoholTypeScent(String variable) {
        this(AlcoholTypeScent.class, forVariable(variable), INITS);
    }

    public QAlcoholTypeScent(Path<? extends AlcoholTypeScent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlcoholTypeScent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlcoholTypeScent(PathMetadata metadata, PathInits inits) {
        this(AlcoholTypeScent.class, metadata, inits);
    }

    public QAlcoholTypeScent(Class<? extends AlcoholTypeScent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholType = inits.isInitialized("alcoholType") ? new QAlcoholType(forProperty("alcoholType")) : null;
        this.category = inits.isInitialized("category") ? new com.juu.juulabel.category.domain.QCategory(forProperty("category"), inits.get("category")) : null;
        this.scent = inits.isInitialized("scent") ? new QScent(forProperty("scent")) : null;
    }

}


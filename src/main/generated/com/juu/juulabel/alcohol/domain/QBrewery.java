package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBrewery is a Querydsl query type for Brewery
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBrewery extends EntityPathBase<Brewery> {

    private static final long serialVersionUID = -570077389L;

    public static final QBrewery brewery = new QBrewery("brewery");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final StringPath name = createString("name");

    public final StringPath region = createString("region");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBrewery(String variable) {
        super(Brewery.class, forVariable(variable));
    }

    public QBrewery(Path<? extends Brewery> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBrewery(PathMetadata metadata) {
        super(Brewery.class, metadata);
    }

}


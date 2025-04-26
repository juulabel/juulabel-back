package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScent is a Querydsl query type for Scent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScent extends EntityPathBase<Scent> {

    private static final long serialVersionUID = 2061584612L;

    public static final QScent scent = new QScent("scent");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QScent(String variable) {
        super(Scent.class, forVariable(variable));
    }

    public QScent(Path<? extends Scent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScent(PathMetadata metadata) {
        super(Scent.class, metadata);
    }

}


package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFlavor is a Querydsl query type for Flavor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlavor extends EntityPathBase<Flavor> {

    private static final long serialVersionUID = -879365259L;

    public static final QFlavor flavor = new QFlavor("flavor");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QFlavor(String variable) {
        super(Flavor.class, forVariable(variable));
    }

    public QFlavor(Path<? extends Flavor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFlavor(PathMetadata metadata) {
        super(Flavor.class, metadata);
    }

}


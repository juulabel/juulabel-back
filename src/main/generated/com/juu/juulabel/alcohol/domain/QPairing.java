package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPairing is a Querydsl query type for Pairing
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPairing extends EntityPathBase<Pairing> {

    private static final long serialVersionUID = -1513074479L;

    public static final QPairing pairing = new QPairing("pairing");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QPairing(String variable) {
        super(Pairing.class, forVariable(variable));
    }

    public QPairing(Path<? extends Pairing> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPairing(PathMetadata metadata) {
        super(Pairing.class, metadata);
    }

}


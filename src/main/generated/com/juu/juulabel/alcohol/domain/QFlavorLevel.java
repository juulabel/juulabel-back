package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlavorLevel is a Querydsl query type for FlavorLevel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlavorLevel extends EntityPathBase<FlavorLevel> {

    private static final long serialVersionUID = -1624270577L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlavorLevel flavorLevel = new QFlavorLevel("flavorLevel");

    public final StringPath description = createString("description");

    public final QFlavor flavor;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public QFlavorLevel(String variable) {
        this(FlavorLevel.class, forVariable(variable), INITS);
    }

    public QFlavorLevel(Path<? extends FlavorLevel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlavorLevel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlavorLevel(PathMetadata metadata, PathInits inits) {
        this(FlavorLevel.class, metadata, inits);
    }

    public QFlavorLevel(Class<? extends FlavorLevel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flavor = inits.isInitialized("flavor") ? new QFlavor(forProperty("flavor")) : null;
    }

}


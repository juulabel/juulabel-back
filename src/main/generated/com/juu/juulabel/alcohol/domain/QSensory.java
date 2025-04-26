package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSensory is a Querydsl query type for Sensory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSensory extends EntityPathBase<Sensory> {

    private static final long serialVersionUID = 1268606472L;

    public static final QSensory sensory = new QSensory("sensory");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QSensory(String variable) {
        super(Sensory.class, forVariable(variable));
    }

    public QSensory(Path<? extends Sensory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSensory(PathMetadata metadata) {
        super(Sensory.class, metadata);
    }

}


package com.juu.juulabel.tastingnote.domain.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlcoholicDrinksSnapshot is a Querydsl query type for AlcoholicDrinksSnapshot
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAlcoholicDrinksSnapshot extends BeanPath<AlcoholicDrinksSnapshot> {

    private static final long serialVersionUID = 579942130L;

    public static final QAlcoholicDrinksSnapshot alcoholicDrinksSnapshot = new QAlcoholicDrinksSnapshot("alcoholicDrinksSnapshot");

    public final NumberPath<Double> alcoholContent = createNumber("alcoholContent", Double.class);

    public final StringPath alcoholicDrinksName = createString("alcoholicDrinksName");

    public final StringPath alcoholTypeName = createString("alcoholTypeName");

    public final StringPath breweryName = createString("breweryName");

    public final StringPath breweryRegion = createString("breweryRegion");

    public QAlcoholicDrinksSnapshot(String variable) {
        super(AlcoholicDrinksSnapshot.class, forVariable(variable));
    }

    public QAlcoholicDrinksSnapshot(Path<? extends AlcoholicDrinksSnapshot> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlcoholicDrinksSnapshot(PathMetadata metadata) {
        super(AlcoholicDrinksSnapshot.class, metadata);
    }

}


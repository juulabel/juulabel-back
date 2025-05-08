package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholicDrinksPairing is a Querydsl query type for AlcoholicDrinksPairing
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholicDrinksPairing extends EntityPathBase<AlcoholicDrinksPairing> {

    private static final long serialVersionUID = 1589617276L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlcoholicDrinksPairing alcoholicDrinksPairing = new QAlcoholicDrinksPairing("alcoholicDrinksPairing");

    public final QAlcoholicDrinks alcoholicDrinks;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPairing pairing;

    public QAlcoholicDrinksPairing(String variable) {
        this(AlcoholicDrinksPairing.class, forVariable(variable), INITS);
    }

    public QAlcoholicDrinksPairing(Path<? extends AlcoholicDrinksPairing> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlcoholicDrinksPairing(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlcoholicDrinksPairing(PathMetadata metadata, PathInits inits) {
        this(AlcoholicDrinksPairing.class, metadata, inits);
    }

    public QAlcoholicDrinksPairing(Class<? extends AlcoholicDrinksPairing> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholicDrinks = inits.isInitialized("alcoholicDrinks") ? new QAlcoholicDrinks(forProperty("alcoholicDrinks"), inits.get("alcoholicDrinks")) : null;
        this.pairing = inits.isInitialized("pairing") ? new QPairing(forProperty("pairing")) : null;
    }

}


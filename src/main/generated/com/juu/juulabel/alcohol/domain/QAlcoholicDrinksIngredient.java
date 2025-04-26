package com.juu.juulabel.alcohol.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlcoholicDrinksIngredient is a Querydsl query type for AlcoholicDrinksIngredient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlcoholicDrinksIngredient extends EntityPathBase<AlcoholicDrinksIngredient> {

    private static final long serialVersionUID = 18141597L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlcoholicDrinksIngredient alcoholicDrinksIngredient = new QAlcoholicDrinksIngredient("alcoholicDrinksIngredient");

    public final QAlcoholicDrinks alcoholicDrinks;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QIngredient ingredient;

    public QAlcoholicDrinksIngredient(String variable) {
        this(AlcoholicDrinksIngredient.class, forVariable(variable), INITS);
    }

    public QAlcoholicDrinksIngredient(Path<? extends AlcoholicDrinksIngredient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlcoholicDrinksIngredient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlcoholicDrinksIngredient(PathMetadata metadata, PathInits inits) {
        this(AlcoholicDrinksIngredient.class, metadata, inits);
    }

    public QAlcoholicDrinksIngredient(Class<? extends AlcoholicDrinksIngredient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholicDrinks = inits.isInitialized("alcoholicDrinks") ? new QAlcoholicDrinks(forProperty("alcoholicDrinks"), inits.get("alcoholicDrinks")) : null;
        this.ingredient = inits.isInitialized("ingredient") ? new QIngredient(forProperty("ingredient")) : null;
    }

}


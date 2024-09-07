package com.juu.juulabel.domain.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAlcoholicDrinks is a Querydsl query type for MemberAlcoholicDrinks
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAlcoholicDrinks extends EntityPathBase<MemberAlcoholicDrinks> {

    private static final long serialVersionUID = 296486953L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAlcoholicDrinks memberAlcoholicDrinks = new QMemberAlcoholicDrinks("memberAlcoholicDrinks");

    public final com.juu.juulabel.domain.base.QBaseCreatedTimeEntity _super = new com.juu.juulabel.domain.base.QBaseCreatedTimeEntity(this);

    public final com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks alcoholicDrinks;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QMemberAlcoholicDrinks(String variable) {
        this(MemberAlcoholicDrinks.class, forVariable(variable), INITS);
    }

    public QMemberAlcoholicDrinks(Path<? extends MemberAlcoholicDrinks> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAlcoholicDrinks(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAlcoholicDrinks(PathMetadata metadata, PathInits inits) {
        this(MemberAlcoholicDrinks.class, metadata, inits);
    }

    public QMemberAlcoholicDrinks(Class<? extends MemberAlcoholicDrinks> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholicDrinks = inits.isInitialized("alcoholicDrinks") ? new com.juu.juulabel.domain.entity.alcohol.QAlcoholicDrinks(forProperty("alcoholicDrinks"), inits.get("alcoholicDrinks")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}


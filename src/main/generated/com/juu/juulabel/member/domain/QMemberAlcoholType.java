package com.juu.juulabel.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAlcoholType is a Querydsl query type for MemberAlcoholType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAlcoholType extends EntityPathBase<MemberAlcoholType> {

    private static final long serialVersionUID = 2046452069L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAlcoholType memberAlcoholType = new QMemberAlcoholType("memberAlcoholType");

    public final com.juu.juulabel.common.base.QBaseTimeEntity _super = new com.juu.juulabel.common.base.QBaseTimeEntity(this);

    public final com.juu.juulabel.alcohol.domain.QAlcoholType alcoholType;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberAlcoholType(String variable) {
        this(MemberAlcoholType.class, forVariable(variable), INITS);
    }

    public QMemberAlcoholType(Path<? extends MemberAlcoholType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAlcoholType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAlcoholType(PathMetadata metadata, PathInits inits) {
        this(MemberAlcoholType.class, metadata, inits);
    }

    public QMemberAlcoholType(Class<? extends MemberAlcoholType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alcoholType = inits.isInitialized("alcoholType") ? new com.juu.juulabel.alcohol.domain.QAlcoholType(forProperty("alcoholType")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}


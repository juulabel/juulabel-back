package com.juu.juulabel.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithdrawalRecord is a Querydsl query type for WithdrawalRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdrawalRecord extends EntityPathBase<WithdrawalRecord> {

    private static final long serialVersionUID = 1674934671L;

    public static final QWithdrawalRecord withdrawalRecord = new QWithdrawalRecord("withdrawalRecord");

    public final com.juu.juulabel.common.base.QBaseCreatedTimeEntity _super = new com.juu.juulabel.common.base.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath withdrawalReason = createString("withdrawalReason");

    public QWithdrawalRecord(String variable) {
        super(WithdrawalRecord.class, forVariable(variable));
    }

    public QWithdrawalRecord(Path<? extends WithdrawalRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithdrawalRecord(PathMetadata metadata) {
        super(WithdrawalRecord.class, metadata);
    }

}


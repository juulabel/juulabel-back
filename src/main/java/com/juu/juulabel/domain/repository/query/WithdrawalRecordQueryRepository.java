package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.entity.member.QWithdrawalRecord;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WithdrawalRecordQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QWithdrawalRecord withdrawalRecord = QWithdrawalRecord.withdrawalRecord;

    public boolean existEmail(String email) {
        return jpaQueryFactory
            .selectOne()
            .from(withdrawalRecord)
            .where(
                withdrawalRecord.email.eq(email)
            )
            .fetchFirst() != null;
    }
}

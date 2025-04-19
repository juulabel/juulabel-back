package com.juu.juulabel.member.repository.query;

import com.juu.juulabel.member.domain.QWithdrawalRecord;
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

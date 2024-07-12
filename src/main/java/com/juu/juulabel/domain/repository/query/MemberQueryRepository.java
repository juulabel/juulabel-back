package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QMember member = QMember.member;

    public Member getByProviderId(String providerId) {
        Member activeMember = jpaQueryFactory
            .selectFrom(member)
            .where(
                member.providerId.eq(providerId),
                member.deletedAt.isNull()
            )
            .fetchOne();

        return Optional.ofNullable(activeMember)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }
    // find로 Optinal 감싸서 서비스 로직에서 검열

}

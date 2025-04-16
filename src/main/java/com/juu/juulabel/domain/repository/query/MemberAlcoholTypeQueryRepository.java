package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.QMemberAlcoholType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberAlcoholTypeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QMemberAlcoholType memberAlcoholType = QMemberAlcoholType.memberAlcoholType;

    public List<Long> findAlcoholTypeIdsByMember(Member member) {
        return jpaQueryFactory
            .select(memberAlcoholType.alcoholType.id)
            .from(memberAlcoholType)
            .where(
                memberAlcoholType.member.eq(member)
            )
            .fetch();
    }

}
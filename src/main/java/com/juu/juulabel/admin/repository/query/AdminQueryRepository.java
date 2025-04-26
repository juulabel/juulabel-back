package com.juu.juulabel.admin.repository.query;

import com.juu.juulabel.admin.response.MemberListSummary;
import com.juu.juulabel.common.dto.request.MemberListRequest;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.domain.QMember;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Slice;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class AdminQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QMember member = QMember.member;

    public Slice<MemberListSummary> getMemberList (Member loginMember, MemberListRequest request, int pageSize){
        List<MemberListSummary> memberList = jpaQueryFactory
                .select(Projections.constructor(MemberListSummary.class,
                        member.id,
                        member.nickname,
                        member.email,
                        member.createdAt,
                        member.status,
                        member.provider,
                        member.hasBadge
                        ))
                .from(member)
                .where(
                        containsNickName(request.nickName()),
                        containsEmail(request.email()),
                        eqStatus(request.status()),
                        eqProvider(request.provider()),
                        eqHasBadge(request.hasBadge())
                )
                .limit(pageSize +1L)
                .fetch();

        boolean hasNext = memberList.size() > pageSize;
        if (hasNext) {
            memberList.remove(pageSize);
        }

        return new SliceImpl<>(memberList, PageRequest.ofSize(pageSize), hasNext);
    }

    private BooleanExpression containsNickName(String nickname){
        return nickname !=null ? member.nickname.contains(nickname) : null;
    }

    private BooleanExpression containsEmail(String email){
        return email !=null ? member.email.contains(email) : null;
    }

    private BooleanExpression eqStatus(MemberStatus status){
        return status!=null ? member.status.eq(status):null;
    }

    private BooleanExpression eqProvider(Provider provider){
        return provider!=null ? member.provider.eq(provider):null;
    }

    private BooleanExpression eqHasBadge(Boolean hasBadge){
        return hasBadge !=null ? member.hasBadge.eq(hasBadge) : null;
    }
}

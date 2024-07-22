package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.follow.FollowUser;
import com.juu.juulabel.domain.entity.follow.QFollow;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.QMember;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QFollow follow = QFollow.follow;
    QMember follower = new QMember("follower");
    QMember followee = new QMember("followee");

    public Slice<FollowUser> findAllFollowing(Member loginMember, Member member, Long lastFollowId, int pageSize) {
        List<FollowUser> followingList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                FollowUser.class,
                                followee.id,
                                followee.nickname,
                                followee.profileImage,
                                jpaQueryFactory
                                        .selectFrom(follow)
                                        .where(
                                                follow.follower.eq(loginMember),
                                                follow.followee.eq(followee)
                                        )
                                        .exists()
                        ))
                .from(follow)
                .innerJoin(follow.follower, follower)
                .innerJoin(follow.followee, followee)
                .where(
                        follower.eq(member),
                        noOffsetByFollowId(follow, lastFollowId)
                )
                .orderBy(followIdDesc(follow))
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = followingList.size() > pageSize;
        if (hasNext) {
            followingList.remove(pageSize);
        }

        return new SliceImpl<>(followingList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<FollowUser> findAllFollower(Member loginMember, Member member, Long lastFollowId, int pageSize) {
        List<FollowUser> followerList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                FollowUser.class,
                                follower.id,
                                follower.nickname,
                                follower.profileImage,
                                jpaQueryFactory
                                        .selectFrom(follow)
                                        .where(
                                                follow.follower.eq(loginMember),
                                                follow.followee.eq(follower)
                                        )
                                        .exists()
                        ))
                .from(follow)
                .innerJoin(follow.follower, follower)
                .innerJoin(follow.followee, followee)
                .where(
                        followee.eq(member),
                        noOffsetByFollowId(follow, lastFollowId)
                )
                .orderBy(followIdDesc(follow))
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = followerList.size() > pageSize;
        if (hasNext) {
            followerList.remove(pageSize);
        }

        return new SliceImpl<>(followerList, PageRequest.ofSize(pageSize), hasNext);
    }

    private OrderSpecifier<Long> followIdDesc(QFollow follow) {
        return follow.id.desc();
    }

    private BooleanExpression noOffsetByFollowId(QFollow follow, Long lastFollowId) {
        return Objects.isEmpty(lastFollowId) ? null : follow.id.lt(lastFollowId);
    }

}

